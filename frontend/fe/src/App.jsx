import React from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import { Toaster as Sonner } from '@/components/ui/sonner';
import { Toaster } from '@/components/ui/toaster';
import { TooltipProvider } from '@/components/ui/tooltip';
import { AuthProvider, useAuth } from '@/hooks/useAuth';
import './index.css';

import Homepage from './pages/Homepage';
import Login from './pages/Login';
import MemberDashboard from './pages/MemberDashboard';
import MemberManagement from './pages/MemberManagement';
import Schedule from './pages/Schedule';
import Branch from './pages/Branch';
import Setting from './pages/Setting';
import Packages from './pages/Packages';
import BookingAdmin from './pages/BookingAdmin';
//import Checkin from './pages/Checkin';

// import BookingUser from './pages/BookingUser';
// import PackagesUser from './pages/PackagesUser';
// import ScheduleUser from './pages/ScheduleUser';
// import Profile from './pages/Profile';
// import Dashboard from './pages/Dashboard';

import AppLayout from './components/AppLayout';
import NotFound from './pages/NotFound';

const queryClient = new QueryClient();

const ProtectedRoute = ({ children, requiredRole = null }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-[#0f1115] text-white">
        <div className="animate-pulse text-lg font-bold">Đang tải...</div>
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/" replace />;
  }

  if (requiredRole) {
    // Role is already normalized in useAuth, direct comparison
    if (user.role !== requiredRole) {
      return <Navigate to={user.role === 'ADMIN' ? '/dashboard' : '/'} replace />;
    }
  }

  return children;
};

const PublicRoute = ({ children }) => {
  const { user, loading } = useAuth();

  if (loading) return null;
  if (user) {
    // Role is already normalized in useAuth
    return <Navigate to={user.role === 'ADMIN' ? '/dashboard' : '/'} replace />;
  }

  return children;
};

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <TooltipProvider>
          <Toaster />
          <Sonner />
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<Homepage />} />
              <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />

              {/* Admin Routes */}
              <Route element={<ProtectedRoute requiredRole="ADMIN"><AppLayout /></ProtectedRoute>}>
                <Route path="/dashboard" element={<MemberDashboard />} />
                <Route path="/members" element={<MemberManagement />} />
                <Route path="/schedule" element={<Schedule />} />
                <Route path="/packages-admin" element={<Packages />} />
                <Route path="/branches" element={<Branch />} />
                <Route path="/settings" element={<Setting />} />
                <Route path="/booking-admin" element={<BookingAdmin />} />
              </Route>

              <Route path="*" element={<NotFound />} />
            </Routes>
          </BrowserRouter>
        </TooltipProvider>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
