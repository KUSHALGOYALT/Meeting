import React from 'react';

const FilterPanel = ({ filter, setFilter, activeTab, onApply }) => (
  <div className="space-y-4">
    <h3 className="text-lg font-semibold">Filters</h3>
    {activeTab === 'skills' && (
      <>
        <div>
          <label className="block text-sm font-medium">Status</label>
          <select
            value={filter.status}
            onChange={(e) => setFilter(prev => ({ ...prev, status: e.target.value }))}
            className="w-full p-2 border rounded"
            aria-label="Filter by status"
          >
            <option value="all">All</option>
            <option value="pending">Pending</option>
            <option value="flagged">Flagged</option>
            <option value="under_review">Under Review</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium">Risk Level</label>
          <select
            value={filter.riskLevel}
            onChange={(e) => setFilter(prev => ({ ...prev, riskLevel: e.target.value }))}
            className="w-full p-2 border rounded"
            aria-label="Filter by risk level"
          >
            <option value="all">All</option>
            <option value="low">Low</option>
            <option value="medium">Medium</option>
            <option value="high">High</option>
          </select>
        </div>
      </>
    )}
    {activeTab === 'content' && (
      <div>
        <label className="block text-sm font-medium">Severity</label>
        <select
          value={filter.severity}
          onChange={(e) => setFilter(prev => ({ ...prev, severity: e.target.value }))}
          className="w-full p-2 border rounded"
          aria-label="Filter by severity"
        >
          <option value="all">All</option>
          <option value="low">Low</option>
          <option value="medium">Medium</option>
          <option value="high">High</option>
        </select>
      </div>
    )}
    {activeTab === 'users' && (
      <div>
        <label className="block text-sm font-medium">Role</label>
        <select
          value={filter.role}
          onChange={(e) => setFilter(prev => ({ ...prev, role: e.target.value }))}
          className="w-full p-2 border rounded"
          aria-label="Filter by role"
        >
          <option value="all">All</option>
          <option value="user">User</option>
          <option value="admin">Admin</option>
          <option value="super_admin">Super Admin</option>
          <option value="moderator">Moderator</option>
        </select>
      </div>
    )}
    <button
      className="w-full bg-primary text-white p-2 rounded hover:bg-dark"
      onClick={onApply}
      aria-label="Apply filters"
    >
      Apply Filters
    </button>
  </div>
);

export default FilterPanel; 