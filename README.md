# Admin Control Panel

A modern React-based admin control panel for managing users, content, and system settings with a beautiful UI and comprehensive functionality.

## 🚀 Features

### 🔐 Authentication
- Secure login with email/password
- JWT token management
- Automatic token refresh
- Secure logout functionality

### 📊 Dashboard
- Real-time statistics and metrics
- User activity overview
- System health monitoring
- Export functionality for reports

### 🛡️ Content Moderation
- Skills approval/rejection system
- Flagged content management
- AI-powered risk assessment
- Content filtering and search

### 👥 User Management
- Complete user listing and management
- User status tracking (active, suspended, banned)
- User suspension and banning capabilities
- Warning system with escalation
- User activation/reactivation

### ⚙️ Settings & Configuration
- AI confidence threshold settings
- Auto-approval configuration
- Content retention policies
- Warning limits and escalation rules

### 🔔 Real-time Features
- WebSocket integration for live notifications
- Toast notifications for user feedback
- Real-time data updates
- Live activity feeds

### 🎨 UI/UX Features
- Dark/light theme toggle
- Responsive design for all devices
- Odoo brand colors and styling
- Loading states and animations
- Search and filtering capabilities
- Pagination for large datasets

### 📱 Additional Features
- Broadcast messaging system
- Export functionality (CSV format)
- Comment system for items
- Modal dialogs for detailed views
- Advanced filtering panels

## 🛠️ Technology Stack

- **Frontend**: React 18.2.0
- **Styling**: Tailwind CSS
- **Icons**: Heroicons
- **HTTP Client**: Axios
- **Date Handling**: date-fns
- **Notifications**: react-toastify
- **Utilities**: lodash.debounce

## 📦 Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd admin-control-panel
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

4. **Build for production**
   ```bash
   npm run build
   ```

## 🔧 Configuration

### Environment Variables
Create a `.env` file in the root directory:

```env
REACT_APP_API_URL=http://localhost:8080
REACT_APP_WS_URL=ws://localhost:8080/ws
REACT_APP_ENVIRONMENT=development
```

### API Configuration
The application is configured to connect to a Spring Boot backend API. Update the API base URL in `src/components/AdminControlPanel.jsx`:

```javascript
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' }
});
```

## 📁 Project Structure

```
src/
├── components/
│   ├── AdminControlPanel.jsx    # Main admin panel component
│   ├── Modal.jsx               # Reusable modal component
│   ├── DataTable.jsx           # Data table component
│   └── FilterPanel.jsx         # Filter panel component
├── App.js                      # Main app component
├── App.css                     # App-specific styles
├── index.js                    # React entry point
└── index.css                   # Global styles and Tailwind imports

public/
├── index.html                  # Main HTML file
└── manifest.json              # PWA manifest

tailwind.config.js             # Tailwind CSS configuration
package.json                   # Dependencies and scripts
```

## 🎨 Customization

### Colors
The application uses Odoo brand colors. You can customize them in:

1. **Tailwind Config** (`tailwind.config.js`):
   ```javascript
   colors: {
     primary: {
       DEFAULT: '#875A7B',
       dark: '#714B67',
       light: '#C7B1C2',
     },
     // ... other colors
   }
   ```

2. **CSS Variables** (`src/index.css`):
   ```css
   :root {
     --primary: #875A7B;
     --dark: #714B67;
     /* ... other variables */
   }
   ```

### Themes
The application supports light and dark themes. Theme switching is handled automatically and persisted in localStorage.

## 🔌 API Integration

The admin panel expects the following API endpoints:

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### Dashboard
- `GET /api/admin/dashboard/stats` - Dashboard statistics

### Skills Management
- `GET /api/admin/skills/pending` - Get pending skills
- `POST /api/admin/skills/{id}/approve` - Approve skill
- `POST /api/admin/skills/{id}/reject` - Reject skill

### Content Moderation
- `GET /api/admin/content/flagged` - Get flagged content
- `POST /api/admin/content/{id}/remove` - Remove content
- `POST /api/admin/content/{id}/ignore` - Ignore flagged content

### User Management
- `GET /api/admin/users` - Get users list
- `POST /api/admin/users/{id}/suspend` - Suspend user
- `POST /api/admin/users/{id}/ban` - Ban user
- `POST /api/admin/users/{id}/activate` - Activate user
- `POST /api/admin/users/{id}/warn` - Warn user

### Settings
- `GET /api/admin/settings/moderation` - Get moderation settings
- `PUT /api/admin/settings/moderation` - Update moderation settings

### Reports
- `GET /api/admin/reports/{type}?format=csv` - Export reports

## 🚀 Deployment

### Build for Production
```bash
npm run build
```

### Deploy to Static Hosting
The build output in the `build/` folder can be deployed to any static hosting service:

- **Netlify**: Drag and drop the `build/` folder
- **Vercel**: Connect your repository and deploy
- **AWS S3**: Upload the `build/` folder to an S3 bucket
- **GitHub Pages**: Use the `gh-pages` package

### Environment-Specific Builds
```bash
# Development
npm run start

# Production
npm run build

# Test
npm run test
```

## 🧪 Testing

```bash
# Run tests
npm test

# Run tests with coverage
npm test -- --coverage

# Run tests in watch mode
npm test -- --watch
```

## 📝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

## 🔄 Updates

Stay updated with the latest features and bug fixes by:
- Watching the repository
- Following the release notes
- Checking the changelog

---

**Built with ❤️ using React and Tailwind CSS** 