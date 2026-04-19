import React, { useEffect, useState } from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import memberApi from './api/memberAPI';

const AppLayout = () => {
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const loadCurrentUser = async () => {
      try {
        const response = await memberApi.getMe();
        setCurrentUser(response.data);
      } catch (err) {
        console.error('Không thể lấy thông tin người dùng:', err);
      }
    };
    loadCurrentUser();
  }, []);

  return (
    <div style={{ display: 'flex' }} className="h-screen w-full bg-gray-50 overflow-hidden">
      <Sidebar currentUser={currentUser} />

      <main className="flex-1 flex flex-col overflow-auto">
        <Outlet />
      </main>
    </div>
  );
};

export default AppLayout;