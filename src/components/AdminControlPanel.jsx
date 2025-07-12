import React, { useState, useEffect, useMemo, useCallback, lazy, Suspense } from 'react';
import axios from 'axios';
import { format } from 'date-fns';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {
  ShieldCheckIcon,
  UserGroupIcon,
  ClockIcon,
  FlagIcon,
  CheckCircleIcon,
  XCircleIcon,
  SearchIcon,
  FilterIcon,
  BanIcon,
  EyeIcon,
  TrendingUpIcon,
  AlertCircleIcon,
  SettingsIcon,
  BellIcon,
  UserIcon,
  DocumentDownloadIcon,
  MailIcon,
  MoonIcon,
  SunIcon
} from '@heroicons/react/outline';
import debounce from 'lodash.debounce';

// Lazy-loaded components
const Modal = lazy(() => import('./Modal'));
const DataTable = lazy(() => import('./DataTable'));
const FilterPanel = lazy(() => import('./FilterPanel'));

const api = axios.create({
  baseURL: 'http://localhost:3000',
  headers: { 'Content-Type': 'application/json' }
});

const AdminControlPanel = () => {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [searchTerm, setSearchTerm] = useState('');
  const [filter, setFilter] = useState({ status: 'all', riskLevel: 'all', severity: 'all', role: 'all' });
  const [stats, setStats] = useState(null);
  const [skills, setSkills] = useState([]);
  const [flaggedContent, setFlaggedContent] = useState([]);
  const [users, setUsers] = useState([]);
  const [swaps, setSwaps] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [settings, setSettings] = useState({
    badWordsFilter: [],
    aiConfidenceThreshold: 70,
    autoApproveSkills: false,
    flaggedContentRetentionDays: 30,
    maxWarningsBeforeSuspension: 3
  });
  const [pagination, setPagination] = useState({ page: 1, limit: 20, totalPages: 1 });
  const [token, setToken] = useState(localStorage.getItem('adminToken') || '');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [broadcast, setBroadcast] = useState({ title: '', content: '', recipientType: 'all', recipientId: '' });
  const [selectedItem, setSelectedItem] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [theme, setTheme] = useState(localStorage.getItem('theme') || 'light');
  const [isLoading, setIsLoading] = useState(false);
  const [comments, setComments] = useState({});
  const [loginData, setLoginData] = useState({ email: '', password: '' });

  // Odoo brand colors
  const colors = {
    primary: '#875A7B',
    dark: '#714B67',
    light: '#C7B1C2',
    accent: '#F06EAA',
    success: '#00A09D',
    warning: '#F4A300',
    danger: '#EF4B4B',
    info: '#1F8EFA',
    background: theme === 'light' ? '#F5F5F5' : '#1F2937',
    textPrimary: theme === 'light' ? '#333333' : '#F3F4F6',
    textSecondary: theme === 'light' ? '#7F7F7F' : '#9CA3AF',
    sidebarBackground: theme === 'light' ? '#FFFFFF' : '#374151',
    borders: theme === 'light' ? '#E0E0E0' : '#4B5563',
    buttonsPrimary: '#875A7B'
  };

  // WebSocket for real-time notifications
  useEffect(() => {
    if (!token) return;
    const ws = new WebSocket('ws://localhost:3001');
    ws.onopen = () => console.log('WebSocket connected');
    ws.onmessage = (event) => {
      const { type, data } = JSON.parse(event.data);
      if (type === 'INIT_NOTIFICATIONS' || type === 'NEW_NOTIFICATION') {
        setNotifications(prev => [...(type === 'NEW_NOTIFICATION' ? [data] : data), ...prev].slice(0, 50));
        if (type === 'NEW_NOTIFICATION') {
          toast.info(data.message, { position: 'top-right', autoClose: 5000 });
        }
      }
    };
    ws.onclose = () => console.log('WebSocket disconnected');
    return () => ws.close();
  }, [token]);

  // Theme toggle
  useEffect(() => {
    document.documentElement.className = theme;
    localStorage.setItem('theme', theme);
  }, [theme]);

  // Debounced search
  const debouncedFetchData = useMemo(() => debounce(async (params) => {
    setIsLoading(true);
    try {
      let response;
      if (activeTab === 'dashboard') {
        response = await api.get('/api/admin/dashboard/stats');
        setStats(response.data.data);
      } else if (activeTab === 'skills') {
        response = await api.get('/api/admin/skills/pending', { params });
        setSkills(response.data.data.skills);
        setPagination(prev => ({ ...prev, ...response.data.data.pagination }));
      } else if (activeTab === 'content') {
        response = await api.get('/api/admin/content/flagged', { params });
        setFlaggedContent(response.data.data.flaggedContent);
        setPagination(prev => ({ ...prev, ...response.data.data.pagination }));
      } else if (activeTab === 'users') {
        response = await api.get('/api/admin/users', { params });
        setUsers(response.data.data.users);
        setPagination(prev => ({ ...prev, ...response.data.data.pagination }));
      } else if (activeTab === 'swaps') {
        response = await api.get('/api/admin/swaps', { params });
        setSwaps(response.data.data.swaps);
        setPagination(prev => ({ ...prev, ...response.data.data.pagination }));
      } else if (activeTab === 'settings') {
        response = await api.get('/api/admin/settings/moderation');
        setSettings(response.data.data);
      } else if (activeTab === 'notifications') {
        response = await api.get('/api/admin/notifications', { params });
        setNotifications(response.data.data.notifications);
        setPagination(prev => ({ ...prev, ...response.data.data.pagination }));
      }
    } catch (err) {
      setError(err.response?.data?.error?.message || 'Failed to fetch data');
      toast.error(err.response?.data?.error?.message || 'Failed to fetch data');
    } finally {
      setIsLoading(false);
    }
  }, 300), [activeTab, token]);

  useEffect(() => {
    if (!token) return;
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    const params = { page: pagination.page, limit: pagination.limit, ...filter };
    if (searchTerm) params.search = searchTerm;
    debouncedFetchData(params);
  }, [activeTab, searchTerm, filter, pagination.page, pagination.limit, debouncedFetchData, token]);

  // Handle actions (approve, reject, suspend, ban, etc.)
  const handleAction = useCallback(async (action, id, data = {}) => {
    setIsLoading(true);
    try {
      let response;
      switch (action) {
        case 'approveSkill':
          response = await api.post(`/api/admin/skills/${id}/approve`, data);
          break;
        case 'rejectSkill':
          response = await api.post(`/api/admin/skills/${id}/reject`, data);
          break;
        case 'removeContent':
          response = await api.post(`/api/admin/content/${id}/remove`, data);
          break;
        case 'ignoreContent':
          response = await api.post(`/api/admin/content/${id}/ignore`, data);
          break;
        case 'suspendUser':
          response = await api.post(`/api/admin/users/${id}/suspend`, data);
          break;
        case 'banUser':
          response = await api.post(`/api/admin/users/${id}/ban`, data);
          break;
        case 'warnUser':
          response = await api.post(`/api/admin/users/${id}/warn`, data);
          break;
        case 'activateUser':
          response = await api.post(`/api/admin/users/${id}/activate`, data);
          break;
        case 'sendBroadcast':
          response = await api.post('/api/admin/messages/broadcast', data);
          break;
        case 'updateSettings':
          response = await api.put('/api/admin/settings/moderation', data);
          break;
        case 'markRead':
          response = await api.post(`/api/admin/notifications/${id}/read`);
          break;
        case 'export':
          response = await api.get(`/api/admin/reports/export?type=${id}&format=csv`, { responseType: 'blob' });
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', `${id}_report_${Date.now()}.csv`);
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          return; // Don't show success message for exports
        default:
          throw new Error('Unknown action');
      }
      setSuccess(response.data.message);
      toast.success(response.data.message);
      debouncedFetchData({ page: pagination.page, limit: pagination.limit, ...filter, search: searchTerm });
      setIsModalOpen(false);
    } catch (err) {
      setError(err.response?.data?.error?.message || 'Action failed');
      toast.error(err.response?.data?.error?.message || 'Action failed');
    } finally {
      setIsLoading(false);
    }
  }, [debouncedFetchData, pagination.page, pagination.limit, filter, searchTerm]);

  // Handle login
  const handleLogin = async () => {
    if (!loginData.email || !loginData.password) {
      setError('Please enter both email and password');
      return;
    }
    
    try {
      const response = await api.post('/api/admin/auth/login', loginData);
      const { token, user } = response.data.data;
      setToken(token);
      localStorage.setItem('adminToken', token);
      setSuccess('Logged in successfully');
      toast.success('Logged in successfully');
      setLoginData({ email: '', password: '' });
    } catch (err) {
      setError(err.response?.data?.error?.message || 'Login failed');
      toast.error(err.response?.data?.error?.message || 'Login failed');
    }
  };

  // Handle comments
  const handleAddComment = (itemId, comment) => {
    setComments(prev => ({
      ...prev,
      [itemId]: [...(prev[itemId] || []), { text: comment, user: 'Admin', timestamp: new Date() }]
    }));
  };

  // Table columns
  const skillColumns = useMemo(() => [
    { header: 'Skill Name', accessor: 'skillName' },
    { header: 'User', accessor: 'userName' },
    { header: 'Status', accessor: 'status', render: (value) => (
      <span className={`px-2 py-1 rounded text-sm ${value === 'pending' ? 'bg-yellow-100 text-yellow-800' : value === 'approved' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
        {value}
      </span>
    )},
    { header: 'Risk Level', accessor: 'riskLevel', render: (value) => (
      <span className={`px-2 py-1 rounded text-sm ${value === 'high' ? 'bg-red-100 text-red-800' : value === 'medium' ? 'bg-yellow-100 text-yellow-800' : 'bg-green-100 text-green-800'}`}>
        {value}
      </span>
    )},
    { header: 'AI Confidence', accessor: 'aiConfidence', render: (value) => `${value}%` },
    { header: 'Submitted', accessor: 'submittedAt', render: (value) => format(new Date(value), 'PPP') },
    {
      header: 'Actions',
      render: (_, row) => (
        <div className="flex space-x-2">
          <button
            className="p-1 text-green-600 hover:text-green-800"
            onClick={() => { setSelectedItem(row); setIsModalOpen(true); }}
            aria-label="View skill details"
          >
            <EyeIcon className="h-5 w-5" />
          </button>
          <button
            className="p-1 text-blue-600 hover:text-blue-800"
            onClick={() => handleAction('approveSkill', row.id, { notes: 'Approved' })}
            disabled={isLoading}
            aria-label="Approve skill"
          >
            <CheckCircleIcon className="h-5 w-5" />
          </button>
          <button
            className="p-1 text-red-600 hover:text-red-800"
            onClick={() => handleAction('rejectSkill', row.id, { reason: 'Inappropriate content', notes: '' })}
            disabled={isLoading}
            aria-label="Reject skill"
          >
            <XCircleIcon className="h-5 w-5" />
          </button>
        </div>
      )
    }
  ], [handleAction, isLoading]);

  const contentColumns = useMemo(() => [
    { header: 'Type', accessor: 'type' },
    { header: 'User', accessor: 'userName' },
    { header: 'Content', accessor: 'content', render: (value) => value.slice(0, 50) + (value.length > 50 ? '...' : '') },
    { header: 'Reason', accessor: 'reason' },
    { header: 'Severity', accessor: 'severity', render: (value) => (
      <span className={`px-2 py-1 rounded text-sm ${value === 'high' ? 'bg-red-100 text-red-800' : value === 'medium' ? 'bg-yellow-100 text-yellow-800' : 'bg-green-100 text-green-800'}`}>
        {value}
      </span>
    )},
    { header: 'Flagged At', accessor: 'flaggedAt', render: (value) => format(new Date(value), 'PPP') },
    {
      header: 'Actions',
      render: (_, row) => (
        <div className="flex space-x-2">
          <button
            className="p-1 text-green-600 hover:text-green-800"
            onClick={() => { setSelectedItem(row); setIsModalOpen(true); }}
            aria-label="View content details"
          >
            <EyeIcon className="h-5 w-5" />
          </button>
          <button
            className="p-1 text-red-600 hover:text-red-800"
            onClick={() => handleAction('removeContent', row.id, { reason: 'Inappropriate' })}
            disabled={isLoading}
            aria-label="Remove content"
          >
            <XCircleIcon className="h-5 w-5" />
          </button>
          <button
            className="p-1 text-blue-600 hover:text-blue-800"
            onClick={() => handleAction('ignoreContent', row.id, { notes: 'False positive' })}
            disabled={isLoading}
            aria-label="Ignore content"
          >
            <CheckCircleIcon className="h-5 w-5" />
          </button>
        </div>
      )
    }
  ], [handleAction, isLoading]);

  const userColumns = useMemo(() => [
    { header: 'Name', accessor: 'name' },
    { header: 'Email', accessor: 'email' },
    { header: 'Status', accessor: 'status', render: (value) => (
      <span className={`px-2 py-1 rounded text-sm ${value === 'active' ? 'bg-green-100 text-green-800' : value === 'suspended' ? 'bg-yellow-100 text-yellow-800' : 'bg-red-100 text-red-800'}`}>
        {value}
      </span>
    )},
    { header: 'Skills', accessor: 'skillCount' },
    { header: 'Swaps', accessor: 'completedSwaps' },
    { header: 'Rating', accessor: 'rating', render: (value) => `${value}/5` },
    { header: 'Join Date', accessor: 'joinDate', render: (value) => format(new Date(value), 'PPP') },
    {
      header: 'Actions',
      render: (_, row) => (
        <div className="flex space-x-2">
          <button
            className="p-1 text-green-600 hover:text-green-800"
            onClick={() => { setSelectedItem(row); setIsModalOpen(true); }}
            aria-label="View user details"
          >
            <EyeIcon className="h-5 w-5" />
          </button>
          {row.status !== 'active' && (
            <button
              className="p-1 text-blue-600 hover:text-blue-800"
              onClick={() => handleAction('activateUser', row.id, { notes: 'Reactivated' })}
              disabled={isLoading}
              aria-label="Activate user"
            >
              <CheckCircleIcon className="h-5 w-5" />
            </button>
          )}
          {row.status !== 'suspended' && (
            <button
              className="p-1 text-yellow-600 hover:text-yellow-800"
              onClick={() => handleAction('suspendUser', row.id, { duration: '7 days', reason: 'Policy violation' })}
              disabled={isLoading}
              aria-label="Suspend user"
            >
              <ClockIcon className="h-5 w-5" />
            </button>
          )}
          {row.status !== 'banned' && (
            <button
              className="p-1 text-red-600 hover:text-red-800"
              onClick={() => handleAction('banUser', row.id, { reason: 'Severe violation' })}
              disabled={isLoading}
              aria-label="Ban user"
            >
              <BanIcon className="h-5 w-5" />
            </button>
          )}
          <button
            className="p-1 text-orange-600 hover:text-orange-800"
            onClick={() => handleAction('warnUser', row.id, { reason: 'Minor violation', message: 'Please review platform guidelines' })}
            disabled={isLoading}
            aria-label="Warn user"
          >
            <AlertCircleIcon className="h-5 w-5" />
          </button>
        </div>
      )
    }
  ], [handleAction, isLoading]);

  const swapColumns = useMemo(() => [
    { header: 'Requester', accessor: 'requester.name' },
    { header: 'Provider', accessor: 'provider.name' },
    { header: 'Requester Skill', accessor: 'requesterSkill' },
    { header: 'Provider Skill', accessor: 'providerSkill' },
    { header: 'Status', accessor: 'status', render: (value) => (
      <span className={`px-2 py-1 rounded text-sm ${value === 'accepted' ? 'bg-green-100 text-green-800' : value === 'pending' ? 'bg-yellow-100 text-yellow-800' : 'bg-red-100 text-red-800'}`}>
        {value}
      </span>
    )},
    { header: 'Start Date', accessor: 'startDate', render: (value) => value ? format(new Date(value), 'PPP') : 'N/A' },
    {
      header: 'Actions',
      render: (_, row) => (
        <div className="flex space-x-2">
          <button
            className="p-1 text-green-600 hover:text-green-800"
            onClick={() => { setSelectedItem(row); setIsModalOpen(true); }}
            aria-label="View swap details"
          >
            <EyeIcon className="h-5 w-5" />
          </button>
        </div>
      )
    }
  ], [isLoading]);

  // Render sidebar
  const renderSidebar = () => (
    <div className={`w-64 ${colors.sidebarBackground} border-r ${colors.borders} h-screen fixed top-0 left-0 flex flex-col p-4 shadow-lg`}>
      <div className="flex items-center justify-between mb-6">
        <h1 className={`text-xl font-bold ${colors.textPrimary}`}>SkillSwap Admin</h1>
        <button
          className="p-2 rounded-full hover:bg-gray-200"
          onClick={() => setTheme(theme === 'light' ? 'dark' : 'light')}
          aria-label="Toggle theme"
        >
          {theme === 'light' ? <MoonIcon className="h-6 w-6" /> : <SunIcon className="h-6 w-6" />}
        </button>
      </div>
      <nav className="flex-1">
        {[
          { name: 'Dashboard', icon: TrendingUpIcon, tab: 'dashboard' },
          { name: 'Skills', icon: ShieldCheckIcon, tab: 'skills' },
          { name: 'Content', icon: FlagIcon, tab: 'content' },
          { name: 'Users', icon: UserGroupIcon, tab: 'users' },
          { name: 'Swaps', icon: ClockIcon, tab: 'swaps' },
          { name: 'Settings', icon: SettingsIcon, tab: 'settings' },
          { name: 'Notifications', icon: BellIcon, tab: 'notifications' }
        ].map(item => (
          <button
            key={item.tab}
            className={`w-full flex items-center p-2 mb-2 rounded ${activeTab === item.tab ? 'bg-primary text-white' : `${colors.textSecondary} hover:bg-gray-200`}`}
            onClick={() => { setActiveTab(item.tab); setPagination(prev => ({ ...prev, page: 1 })); }}
            aria-label={`Go to ${item.name}`}
          >
            <item.icon className="h-5 w-5 mr-2" />
            {item.name}
            {item.tab === 'notifications' && notifications.filter(n => !n.isRead).length > 0 && (
              <span className="ml-auto bg-red-500 text-white text-xs rounded-full px-2 py-1">
                {notifications.filter(n => !n.isRead).length}
              </span>
            )}
          </button>
        ))}
      </nav>
      <button
        className={`mt-auto p-2 rounded ${colors.textSecondary} hover:bg-gray-200`}
        onClick={() => { localStorage.removeItem('adminToken'); setToken(''); }}
        aria-label="Logout"
      >
        Logout
      </button>
    </div>
  );

  // Render login form
  const renderLogin = () => (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6 text-center text-primary">Admin Login</h2>
        <div className="space-y-4">
          <input
            type="email"
            placeholder="Email"
            value={loginData.email}
            onChange={(e) => setLoginData(prev => ({ ...prev, email: e.target.value }))}
            className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
            aria-label="Email input"
          />
          <input
            type="password"
            placeholder="Password"
            value={loginData.password}
            onChange={(e) => setLoginData(prev => ({ ...prev, password: e.target.value }))}
            onKeyPress={(e) => e.key === 'Enter' && handleLogin()}
            className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
            aria-label="Password input"
          />
          <button
            className="w-full bg-primary text-white p-2 rounded hover:bg-dark disabled:bg-gray-400"
            onClick={handleLogin}
            disabled={isLoading}
            aria-label="Login"
          >
            Login
          </button>
          {error && <p className="text-danger text-sm">{error}</p>}
          {success && <p className="text-success text-sm">{success}</p>}
        </div>
      </div>
    </div>
  );

  // Render dashboard
  const renderDashboard = () => (
    <div className="p-6">
      <h2 className={`text-2xl font-bold mb-4 ${colors.textPrimary}`}>Dashboard</h2>
      {stats && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {[
            { title: 'Total Users', value: stats.totalUsers, change: stats.changes.totalUsers, color: 'info' },
            { title: 'Pending Reviews', value: stats.pendingReviews, change: stats.changes.pendingReviews, color: 'warning' },
            { title: 'Flagged Content', value: stats.flaggedContent, change: stats.changes.flaggedContent, color: 'danger' },
            { title: 'Verified Skills', value: stats.verifiedSkills, change: stats.changes.verifiedSkills, color: 'success' },
            { title: 'Active Swaps', value: stats.activeSwaps, change: stats.changes.activeSwaps, color: 'accent' }
          ].map(stat => (
            <div key={stat.title} className={`p-4 rounded-lg shadow bg-white ${colors[stat.color]} text-white`}>
              <h3 className="text-lg font-semibold">{stat.title}</h3>
              <p className="text-2xl">{stat.value}</p>
              <p className="text-sm">{stat.change}</p>
            </div>
          ))}
        </div>
      )}
      <button
        className="mt-4 bg-primary text-white px-4 py-2 rounded hover:bg-dark"
        onClick={() => handleAction('export', 'dashboard')}
        disabled={isLoading}
        aria-label="Export dashboard data"
      >
        <DocumentDownloadIcon className="h-5 w-5 inline mr-2" /> Export Dashboard
      </button>
    </div>
  );

  // Render settings
  const renderSettings = () => (
    <div className="p-6">
      <h2 className={`text-2xl font-bold mb-4 ${colors.textPrimary}`}>Settings</h2>
      <div className="bg-white p-4 rounded-lg shadow">
        <h3 className={`text-lg font-semibold mb-4 ${colors.textPrimary}`}>Moderation Settings</h3>
        <div className="space-y-4">
          <div>
            <label className={`block text-sm font-medium ${colors.textPrimary}`}>AI Confidence Threshold</label>
            <input
              type="number"
              value={settings.aiConfidenceThreshold}
              onChange={(e) => setSettings(prev => ({ ...prev, aiConfidenceThreshold: parseInt(e.target.value) }))}
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
              aria-label="AI Confidence Threshold"
            />
          </div>
          <div>
            <label className={`block text-sm font-medium ${colors.textPrimary}`}>Auto Approve Skills</label>
            <input
              type="checkbox"
              checked={settings.autoApproveSkills}
              onChange={(e) => setSettings(prev => ({ ...prev, autoApproveSkills: e.target.checked }))}
              className="h-4 w-4 text-primary focus:ring-primary"
              aria-label="Auto Approve Skills"
            />
          </div>
          <div>
            <label className={`block text-sm font-medium ${colors.textPrimary}`}>Flagged Content Retention (Days)</label>
            <input
              type="number"
              value={settings.flaggedContentRetentionDays}
              onChange={(e) => setSettings(prev => ({ ...prev, flaggedContentRetentionDays: parseInt(e.target.value) }))}
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
              aria-label="Flagged Content Retention Days"
            />
          </div>
          <div>
            <label className={`block text-sm font-medium ${colors.textPrimary}`}>Max Warnings Before Suspension</label>
            <input
              type="number"
              value={settings.maxWarningsBeforeSuspension}
              onChange={(e) => setSettings(prev => ({ ...prev, maxWarningsBeforeSuspension: parseInt(e.target.value) }))}
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
              aria-label="Max Warnings Before Suspension"
            />
          </div>
          <button
            className="bg-primary text-white px-4 py-2 rounded hover:bg-dark disabled:bg-gray-400"
            onClick={() => handleAction('updateSettings', null, settings)}
            disabled={isLoading}
            aria-label="Save Settings"
          >
            Save Settings
          </button>
        </div>
      </div>
    </div>
  );

  // Render broadcast
  const renderBroadcast = () => (
    <div className="p-6">
      <h2 className={`text-2xl font-bold mb-4 ${colors.textPrimary}`}>Send Broadcast Message</h2>
      <div className="bg-white p-4 rounded-lg shadow">
        <div className="space-y-4">
          <div>
            <label className={`block text-sm font-medium ${colors.textPrimary}`}>Title</label>
            <input
              type="text"
              value={broadcast.title}
              onChange={(e) => setBroadcast(prev => ({ ...prev, title: e.target.value }))}
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
              aria-label="Broadcast Title"
            />
          </div>
          <div>
            <label className={`block text-sm font-medium ${colors.textPrimary}`}>Content</label>
            <textarea
              value={broadcast.content}
              onChange={(e) => setBroadcast(prev => ({ ...prev, content: e.target.value }))}
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
              rows="4"
              aria-label="Broadcast Content"
            />
          </div>
          <div>
            <label className={`block text-sm font-medium ${colors.textPrimary}`}>Recipient Type</label>
            <select
              value={broadcast.recipientType}
              onChange={(e) => setBroadcast(prev => ({ ...prev, recipientType: e.target.value }))}
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
              aria-label="Recipient Type"
            >
              <option value="all">All Users</option>
              <option value="specific">Specific User</option>
              <option value="role">By Role</option>
            </select>
          </div>
          {broadcast.recipientType === 'specific' && (
            <div>
              <label className={`block text-sm font-medium ${colors.textPrimary}`}>Recipient ID</label>
              <input
                type="text"
                value={broadcast.recipientId}
                onChange={(e) => setBroadcast(prev => ({ ...prev, recipientId: e.target.value }))}
                className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
                aria-label="Recipient ID"
              />
            </div>
          )}
          <button
            className="bg-primary text-white px-4 py-2 rounded hover:bg-dark disabled:bg-gray-400"
            onClick={() => handleAction('sendBroadcast', null, broadcast)}
            disabled={isLoading || !broadcast.title || !broadcast.content}
            aria-label="Send Broadcast"
          >
            <MailIcon className="h-5 w-5 inline mr-2" /> Send Broadcast
          </button>
        </div>
      </div>
    </div>
  );

  // Render notifications
  const renderNotifications = () => (
    <div className="p-6">
      <h2 className={`text-2xl font-bold mb-4 ${colors.textPrimary}`}>Notifications</h2>
      <div className="bg-white p-4 rounded-lg shadow">
        {notifications.length === 0 ? (
          <p className="text-center text-gray-500">No notifications</p>
        ) : (
          notifications.map(notification => (
            <div
              key={notification._id}
              className={`p-3 border-b ${colors.borders} flex justify-between items-center ${notification.isRead ? 'bg-gray-50' : 'bg-white'}`}
            >
              <div>
                <h3 className={`font-semibold ${colors.textPrimary}`}>{notification.title}</h3>
                <p className={colors.textSecondary}>{notification.message}</p>
                <p className={`text-sm ${colors.textSecondary}`}>{format(new Date(notification.createdAt), 'PPP p')}</p>
              </div>
              {!notification.isRead && (
                <button
                  className="text-blue-600 hover:text-blue-800"
                  onClick={() => handleAction('markRead', notification._id)}
                  aria-label="Mark notification as read"
                >
                  Mark as Read
                </button>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );

  // Render main content
  const renderContent = () => (
    <div className="p-6">
      <div className="flex justify-between items-center mb-4">
        <h2 className={`text-2xl font-bold ${colors.textPrimary}`}>
          {activeTab.charAt(0).toUpperCase() + activeTab.slice(1)}
        </h2>
        <div className="flex space-x-2">
          <div className="relative">
            <input
              type="text"
              placeholder="Search..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="p-2 pl-8 border rounded focus:outline-none focus:ring-2 focus:ring-primary"
              aria-label="Search"
            />
            <SearchIcon className="h-5 w-5 absolute left-2 top-2.5 text-gray-400" />
          </div>
          <button
            className="p-2 bg-primary text-white rounded hover:bg-dark"
            onClick={() => setIsModalOpen(true)}
            aria-label="Open filters"
          >
            <FilterIcon className="h-5 w-5" />
          </button>
          {['skills', 'users', 'content'].includes(activeTab) && (
            <button
              className="p-2 bg-primary text-white rounded hover:bg-dark"
              onClick={() => handleAction('export', activeTab)}
              disabled={isLoading}
              aria-label={`Export ${activeTab} data`}
            >
              <DocumentDownloadIcon className="h-5 w-5" />
            </button>
          )}
        </div>
      </div>
      <Suspense fallback={<div>Loading...</div>}>
        {activeTab === 'skills' && (
          <DataTable
            columns={skillColumns}
            data={skills}
            pagination={pagination}
            setPagination={setPagination}
            isLoading={isLoading}
          />
        )}
        {activeTab === 'content' && (
          <DataTable
            columns={contentColumns}
            data={flaggedContent}
            pagination={pagination}
            setPagination={setPagination}
            isLoading={isLoading}
          />
        )}
        {activeTab === 'users' && (
          <DataTable
            columns={userColumns}
            data={users}
            pagination={pagination}
            setPagination={setPagination}
            isLoading={isLoading}
          />
        )}
        {activeTab === 'swaps' && (
          <DataTable
            columns={swapColumns}
            data={swaps}
            pagination={pagination}
            setPagination={setPagination}
            isLoading={isLoading}
          />
        )}
        {activeTab === 'settings' && renderSettings()}
        {activeTab === 'notifications' && renderNotifications()}
        {activeTab === 'broadcast' && renderBroadcast()}
      </Suspense>
    </div>
  );

  // Render modal
  const renderModal = () => (
    <Suspense fallback={<div>Loading...</div>}>
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={selectedItem ? `${activeTab.charAt(0).toUpperCase() + activeTab.slice(1)} Details` : 'Filters'}
      >
        {selectedItem ? (
          <div>
            <h3 className={`text-lg font-semibold ${colors.textPrimary}`}>Details</h3>
            <pre className="bg-gray-100 p-4 rounded overflow-auto max-h-96">{JSON.stringify(selectedItem, null, 2)}</pre>
            <div className="mt-4">
              <h4 className={`text-md font-semibold ${colors.textPrimary}`}>Comments</h4>
              {comments[selectedItem.id]?.map((comment, index) => (
                <div key={index} className={`p-2 border-b ${colors.borders}`}>
                  <p className={colors.textSecondary}>{comment.text}</p>
                  <p className={`text-sm ${colors.textSecondary}`}>{comment.user} - {format(new Date(comment.timestamp), 'PPP p')}</p>
                </div>
              ))}
              <textarea
                className="w-full p-2 border rounded mt-2"
                placeholder="Add a comment..."
                onChange={(e) => handleAddComment(selectedItem.id, e.target.value)}
                aria-label="Add comment"
              />
            </div>
          </div>
        ) : (
          <FilterPanel
            filter={filter}
            setFilter={setFilter}
            activeTab={activeTab}
            onApply={() => setIsModalOpen(false)}
          />
        )}
      </Modal>
    </Suspense>
  );

  return (
    <div className={`min-h-screen ${colors.background}`}>
      <style jsx global>{`
        :root {
          --primary: ${colors.primary};
          --dark: ${colors.dark};
          --light: ${colors.light};
          --accent: ${colors.accent};
          --success: ${colors.success};
          --warning: ${colors.warning};
          --danger: ${colors.danger};
          --info: ${colors.info};
        }
        .dark {
          --background: ${colors.background};
          --text-primary: ${colors.textPrimary};
          --text-secondary: ${colors.textSecondary};
          --sidebar-background: ${colors.sidebarBackground};
          --borders: ${colors.borders};
        }
      `}</style>
      <ToastContainer />
      {!token ? renderLogin() : (
        <div className="flex">
          {renderSidebar()}
          <div className="ml-64 w-full">
            {activeTab === 'dashboard' ? renderDashboard() : renderContent()}
          </div>
          {isModalOpen && renderModal()}
        </div>
      )}
    </div>
  );
};

export default AdminControlPanel; 