from fastapi import FastAPI, HTTPException, Depends, Query
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy import create_engine, Column, Integer, String, DateTime, Text, ForeignKey, Table, Float, Boolean
from sqlalchemy.orm import declarative_base
from sqlalchemy.orm import sessionmaker, Session, relationship
from sqlalchemy.dialects.postgresql import ENUM
from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
from datetime import datetime
from enum import Enum as PyEnum
import os
from dotenv import load_dotenv
import uvicorn

load_dotenv()

# Database configuration - Updated with your Render PostgreSQL credentials
DATABASE_URL = "postgresql://skillswap_k0j8_user:2rcJuJ4NYjx98byleuHI0oiteBUHpqbe@dpg-d1ouq2k9c44c7381rfk0-a.oregon-postgres.render.com:5432/skillswap_k0j8"

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

# Enums
class SkillLevel(str, PyEnum):
    beginner = "beginner"
    intermediate = "intermediate"
    expert = "expert"

class ProjectStatus(str, PyEnum):
    active = "active"
    completed = "completed"
    on_hold = "on_hold"

class UserStatus(str, PyEnum):
    available = "available"
    busy = "busy"
    away = "away"

# Create ENUM types for PostgreSQL
skill_level_enum = ENUM(SkillLevel, name="skill_level", create_type=False)
project_status_enum = ENUM(ProjectStatus, name="project_status", create_type=False)
user_status_enum = ENUM(UserStatus, name="user_status", create_type=False)

# Association tables for many-to-many relationships
project_collaborators = Table(
    'project_collaborators',
    Base.metadata,
    Column('project_id', Integer, ForeignKey('projects.id'), primary_key=True),
    Column('user_id', Integer, ForeignKey('users.id'), primary_key=True)
)

project_skills = Table(
    'project_skills',
    Base.metadata,
    Column('project_id', Integer, ForeignKey('projects.id'), primary_key=True),
    Column('skill_id', Integer, ForeignKey('skills.id'), primary_key=True)
)

# SQLAlchemy Models - Order matters for foreign key dependencies
class User(Base):
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, nullable=False)
    username = Column(String, unique=True, index=True, nullable=False)
    email = Column(String, unique=True, index=True, nullable=False)
    avatar = Column(String, default="/placeholder.svg")
    bio = Column(Text)
    status = Column(user_status_enum, default=UserStatus.available)
    availability = Column(String)
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)
    
    # Relationships
    user_skills = relationship("UserSkill", back_populates="user", cascade="all, delete-orphan")
    wanted_skills = relationship("WantedSkill", back_populates="user", cascade="all, delete-orphan")
    projects = relationship("Project", secondary=project_collaborators, back_populates="collaborators")
    stats = relationship("UserStats", back_populates="user", uselist=False, cascade="all, delete-orphan")

class Skill(Base):
    __tablename__ = "skills"
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, index=True, nullable=False)
    description = Column(Text)
    category = Column(String)
    is_popular = Column(Boolean, default=False)
    created_at = Column(DateTime, default=datetime.utcnow)
    
    # Relationships
    user_skills = relationship("UserSkill", back_populates="skill")
    wanted_skills = relationship("WantedSkill", back_populates="skill")
    projects = relationship("Project", secondary=project_skills, back_populates="required_skills")

class UserSkill(Base):
    __tablename__ = "user_skills"
    
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    skill_id = Column(Integer, ForeignKey("skills.id"), nullable=False)
    level = Column(skill_level_enum, nullable=False)
    years_experience = Column(Integer, default=0)
    created_at = Column(DateTime, default=datetime.utcnow)
    
    # Relationships
    user = relationship("User", back_populates="user_skills")
    skill = relationship("Skill", back_populates="user_skills")

class WantedSkill(Base):
    __tablename__ = "wanted_skills"
    
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    skill_id = Column(Integer, ForeignKey("skills.id"), nullable=False)
    priority = Column(Integer, default=1)  # 1-5 scale
    created_at = Column(DateTime, default=datetime.utcnow)
    
    # Relationships
    user = relationship("User", back_populates="wanted_skills")
    skill = relationship("Skill", back_populates="wanted_skills")

class Project(Base):
    __tablename__ = "projects"
    
    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, nullable=False)
    description = Column(Text)
    status = Column(project_status_enum, default=ProjectStatus.active)
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)
    
    # Relationships
    collaborators = relationship("User", secondary=project_collaborators, back_populates="projects")
    required_skills = relationship("Skill", secondary=project_skills, back_populates="projects")

class UserStats(Base):
    __tablename__ = "user_stats"
    
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    skills_swapped = Column(Integer, default=0)
    active_projects = Column(Integer, default=0)
    completed_projects = Column(Integer, default=0)
    profile_views = Column(Integer, default=0)
    reputation_score = Column(Float, default=0.0)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)
    
    # Relationships
    user = relationship("User", back_populates="stats")

# Create ENUM types first
def create_enums():
    """Create custom enum types in PostgreSQL"""
    with engine.connect() as conn:
        # Check if enums exist, create if they don't
        result = conn.execute("""
            SELECT typname FROM pg_type WHERE typname IN ('skill_level', 'project_status', 'user_status')
        """)
        existing_enums = [row[0] for row in result.fetchall()]
        
        if 'skill_level' not in existing_enums:
            conn.execute("CREATE TYPE skill_level AS ENUM ('beginner', 'intermediate', 'expert')")
        
        if 'project_status' not in existing_enums:
            conn.execute("CREATE TYPE project_status AS ENUM ('active', 'completed', 'on_hold')")
        
        if 'user_status' not in existing_enums:
            conn.execute("CREATE TYPE user_status AS ENUM ('available', 'busy', 'away')")
        
        conn.commit()

# Create enums first, then tables
try:
    create_enums()
    Base.metadata.create_all(bind=engine)
except Exception as e:
    print(f"Database setup completed with info: {e}")

# Pydantic Models
class UserSkillResponse(BaseModel):
    name: str
    level: SkillLevel
    years_experience: Optional[int] = 0
    
    class Config:
        from_attributes = True

class WantedSkillResponse(BaseModel):
    name: str
    priority: int
    
    class Config:
        from_attributes = True

class CollaboratorResponse(BaseModel):
    id: int
    name: str
    avatar: str
    
    class Config:
        from_attributes = True

class ProjectResponse(BaseModel):
    id: int
    title: str
    description: Optional[str] = None
    collaborators: List[CollaboratorResponse]
    status: ProjectStatus
    skills: List[str]
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True

class UserProfileResponse(BaseModel):
    id: int
    name: str
    username: str
    email: str
    avatar: str
    bio: Optional[str] = None
    status: UserStatus
    availability: Optional[str] = None
    skills: List[UserSkillResponse]
    wanted_skills: List[WantedSkillResponse]
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True

class UserStatsResponse(BaseModel):
    skills_swapped: int
    active_projects: int
    profile_views: int
    completed_projects: int
    reputation_score: float
    
    class Config:
        from_attributes = True

# Request Models
class CreateUserRequest(BaseModel):
    name: str
    username: str
    email: str
    bio: Optional[str] = None
    avatar: Optional[str] = "/placeholder.svg"

class UpdateProfileRequest(BaseModel):
    name: Optional[str] = None
    bio: Optional[str] = None
    status: Optional[UserStatus] = None
    availability: Optional[str] = None

class AddSkillRequest(BaseModel):
    name: str
    level: SkillLevel
    years_experience: Optional[int] = 0

class AddWantedSkillRequest(BaseModel):
    name: str
    priority: Optional[int] = 1

class CreateProjectRequest(BaseModel):
    title: str
    description: Optional[str] = None
    required_skills: List[str]

class SkillResponse(BaseModel):
    id: int
    name: str
    description: Optional[str] = None
    category: Optional[str] = None
    is_popular: bool
    
    class Config:
        from_attributes = True

# FastAPI app
app = FastAPI(
    title="Skill Swap API",
    description="API for skill swapping and collaboration platform",
    version="1.0.0"
)

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Configure for production
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Dependency to get DB session
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Helper functions
def get_user_by_id(db: Session, user_id: int):
    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return user

def get_skill_by_name(db: Session, skill_name: str):
    return db.query(Skill).filter(Skill.name == skill_name).first()

def create_skill_if_not_exists(db: Session, skill_name: str):
    skill = get_skill_by_name(db, skill_name)
    if not skill:
        skill = Skill(name=skill_name)
        db.add(skill)
        db.commit()
        db.refresh(skill)
    return skill

def update_user_stats(db: Session, user_id: int):
    """Update user statistics based on current data"""
    user = get_user_by_id(db, user_id)
    
    # Count active projects
    active_projects = db.query(Project).join(project_collaborators).filter(
        project_collaborators.c.user_id == user_id,
        Project.status == ProjectStatus.active
    ).count()
    
    # Count completed projects
    completed_projects = db.query(Project).join(project_collaborators).filter(
        project_collaborators.c.user_id == user_id,
        Project.status == ProjectStatus.completed
    ).count()
    
    # Update or create stats
    if user.stats:
        user.stats.active_projects = active_projects
        user.stats.completed_projects = completed_projects
        user.stats.updated_at = datetime.utcnow()
    else:
        stats = UserStats(
            user_id=user_id,
            active_projects=active_projects,
            completed_projects=completed_projects
        )
        db.add(stats)
    
    db.commit()

# API Endpoints
@app.get("/")
async def root():
    return {"message": "Skill Swap API is running with PostgreSQL!"}

@app.post("/users", response_model=UserProfileResponse)
async def create_user(user_request: CreateUserRequest, db: Session = Depends(get_db)):
    """Create a new user"""
    # Check if username or email already exists
    existing_user = db.query(User).filter(
        (User.username == user_request.username) | (User.email == user_request.email)
    ).first()
    
    if existing_user:
        raise HTTPException(status_code=400, detail="Username or email already exists")
    
    user = User(**user_request.dict())
    db.add(user)
    db.commit()
    db.refresh(user)
    
    # Create initial stats
    stats = UserStats(user_id=user.id)
    db.add(stats)
    db.commit()
    
    return user

@app.get("/users/{user_id}", response_model=UserProfileResponse)
async def get_user_profile(user_id: int, db: Session = Depends(get_db)):
    """Get user profile by ID"""
    user = get_user_by_id(db, user_id)
    
    # Update profile views
    if user.stats:
        user.stats.profile_views += 1
        db.commit()
    
    return user

@app.put("/users/{user_id}", response_model=UserProfileResponse)
async def update_user_profile(user_id: int, profile_update: UpdateProfileRequest, db: Session = Depends(get_db)):
    """Update user profile"""
    user = get_user_by_id(db, user_id)
    
    update_data = profile_update.dict(exclude_unset=True)
    for field, value in update_data.items():
        setattr(user, field, value)
    
    user.updated_at = datetime.utcnow()
    db.commit()
    db.refresh(user)
    
    return user

@app.get("/users/{user_id}/stats", response_model=UserStatsResponse)
async def get_user_stats(user_id: int, db: Session = Depends(get_db)):
    """Get user statistics"""
    user = get_user_by_id(db, user_id)
    update_user_stats(db, user_id)
    
    if not user.stats:
        raise HTTPException(status_code=404, detail="User stats not found")
    
    return user.stats

@app.post("/users/{user_id}/skills", response_model=UserSkillResponse)
async def add_user_skill(user_id: int, skill_request: AddSkillRequest, db: Session = Depends(get_db)):
    """Add a skill to user's profile"""
    user = get_user_by_id(db, user_id)
    skill = create_skill_if_not_exists(db, skill_request.name)
    
    # Check if user already has this skill
    existing_skill = db.query(UserSkill).filter(
        UserSkill.user_id == user_id,
        UserSkill.skill_id == skill.id
    ).first()
    
    if existing_skill:
        raise HTTPException(status_code=400, detail="User already has this skill")
    
    user_skill = UserSkill(
        user_id=user_id,
        skill_id=skill.id,
        level=skill_request.level,
        years_experience=skill_request.years_experience
    )
    
    db.add(user_skill)
    db.commit()
    db.refresh(user_skill)
    
    return {
        "name": skill.name,
        "level": user_skill.level,
        "years_experience": user_skill.years_experience
    }

@app.delete("/users/{user_id}/skills/{skill_name}")
async def remove_user_skill(user_id: int, skill_name: str, db: Session = Depends(get_db)):
    """Remove a skill from user's profile"""
    user = get_user_by_id(db, user_id)
    skill = get_skill_by_name(db, skill_name)
    
    if not skill:
        raise HTTPException(status_code=404, detail="Skill not found")
    
    user_skill = db.query(UserSkill).filter(
        UserSkill.user_id == user_id,
        UserSkill.skill_id == skill.id
    ).first()
    
    if not user_skill:
        raise HTTPException(status_code=404, detail="User doesn't have this skill")
    
    db.delete(user_skill)
    db.commit()
    
    return {"message": "Skill removed successfully"}

@app.post("/users/{user_id}/wanted-skills", response_model=WantedSkillResponse)
async def add_wanted_skill(user_id: int, skill_request: AddWantedSkillRequest, db: Session = Depends(get_db)):
    """Add a wanted skill to user's profile"""
    user = get_user_by_id(db, user_id)
    skill = create_skill_if_not_exists(db, skill_request.name)
    
    # Check if user already wants this skill
    existing_skill = db.query(WantedSkill).filter(
        WantedSkill.user_id == user_id,
        WantedSkill.skill_id == skill.id
    ).first()
    
    if existing_skill:
        raise HTTPException(status_code=400, detail="User already wants this skill")
    
    wanted_skill = WantedSkill(
        user_id=user_id,
        skill_id=skill.id,
        priority=skill_request.priority
    )
    
    db.add(wanted_skill)
    db.commit()
    db.refresh(wanted_skill)
    
    return {
        "name": skill.name,
        "priority": wanted_skill.priority
    }

@app.delete("/users/{user_id}/wanted-skills/{skill_name}")
async def remove_wanted_skill(user_id: int, skill_name: str, db: Session = Depends(get_db)):
    """Remove a wanted skill from user's profile"""
    user = get_user_by_id(db, user_id)
    skill = get_skill_by_name(db, skill_name)
    
    if not skill:
        raise HTTPException(status_code=404, detail="Skill not found")
    
    wanted_skill = db.query(WantedSkill).filter(
        WantedSkill.user_id == user_id,
        WantedSkill.skill_id == skill.id
    ).first()
    
    if not wanted_skill:
        raise HTTPException(status_code=404, detail="User doesn't want this skill")
    
    db.delete(wanted_skill)
    db.commit()
    
    return {"message": "Wanted skill removed successfully"}

@app.get("/users/{user_id}/projects", response_model=List[ProjectResponse])
async def get_user_projects(user_id: int, db: Session = Depends(get_db)):
    """Get user's projects"""
    user = get_user_by_id(db, user_id)
    
    projects = db.query(Project).join(project_collaborators).filter(
        project_collaborators.c.user_id == user_id
    ).all()
    
    result = []
    for project in projects:
        result.append({
            "id": project.id,
            "title": project.title,
            "description": project.description,
            "collaborators": [
                {"id": c.id, "name": c.name, "avatar": c.avatar}
                for c in project.collaborators
            ],
            "status": project.status,
            "skills": [s.name for s in project.required_skills],
            "created_at": project.created_at,
            "updated_at": project.updated_at
        })
    
    return result

@app.post("/users/{user_id}/projects", response_model=ProjectResponse)
async def create_project(user_id: int, project_request: CreateProjectRequest, db: Session = Depends(get_db)):
    """Create a new project"""
    user = get_user_by_id(db, user_id)
    
    project = Project(
        title=project_request.title,
        description=project_request.description
    )
    
    # Add creator as collaborator
    project.collaborators.append(user)
    
    # Add required skills
    for skill_name in project_request.required_skills:
        skill = create_skill_if_not_exists(db, skill_name)
        project.required_skills.append(skill)
    
    db.add(project)
    db.commit()
    db.refresh(project)
    
    # Update user stats
    update_user_stats(db, user_id)
    
    return {
        "id": project.id,
        "title": project.title,
        "description": project.description,
        "collaborators": [
            {"id": c.id, "name": c.name, "avatar": c.avatar}
            for c in project.collaborators
        ],
        "status": project.status,
        "skills": [s.name for s in project.required_skills],
        "created_at": project.created_at,
        "updated_at": project.updated_at
    }

@app.put("/projects/{project_id}/status")
async def update_project_status(project_id: int, status: ProjectStatus, db: Session = Depends(get_db)):
    """Update project status"""
    project = db.query(Project).filter(Project.id == project_id).first()
    
    if not project:
        raise HTTPException(status_code=404, detail="Project not found")
    
    project.status = status
    project.updated_at = datetime.utcnow()
    db.commit()
    
    # Update stats for all collaborators
    for collaborator in project.collaborators:
        update_user_stats(db, collaborator.id)
    
    return {"message": "Project status updated successfully"}

@app.get("/skills", response_model=List[SkillResponse])
async def get_skills(
    search: Optional[str] = Query(None, description="Search skills by name"),
    popular: Optional[bool] = Query(None, description="Filter popular skills"),
    limit: Optional[int] = Query(50, description="Limit results"),
    db: Session = Depends(get_db)
):
    """Get skills with optional filtering"""
    query = db.query(Skill)
    
    if search:
        query = query.filter(Skill.name.ilike(f"%{search}%"))
    
    if popular is not None:
        query = query.filter(Skill.is_popular == popular)
    
    skills = query.limit(limit).all()
    return skills

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {"status": "healthy", "timestamp": datetime.utcnow(), "database": "postgresql"}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)