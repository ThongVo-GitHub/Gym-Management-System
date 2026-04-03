import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';
import Homepage from './Homepage';
import Login from './Login';
import AppLayout from './AppLayout'; 
import MemberDashboard from './MemberDashboard'; 
import MemberManagement from './MemberManagement';
import Schedule from './Schedule'; // <--- IMPORT COMPONENT MỚI Ở ĐÂY
import Booking from './Booking'; 
import Packages from './Packages'; 
import Setting from './Setting';
import Branch from './Branch';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* PUBLIC ROUTES */}
        <Route path="/" element={<Homepage />} />
        <Route path="/login" element={<Login />} />

        {/* =========================================
            KHU VỰC ADMIN (Có thanh menu đen bên trái) 
            ========================================= */}
        <Route element={<AppLayout />}>
          <Route path="/dashboard" element={<MemberDashboard />} /> 
          <Route path="/members" element={<MemberManagement />} />
          
          {/* CẬP NHẬT ROUTE CHO LỊCH HỌC TẠI ĐÂY */}
          <Route path="/schedule" element={<Schedule />} /> 
          
          <Route path="/packages-admin" element={<Packages />} />
          <Route path="/branches" element={<Branch />} />
          <Route path="/settings" element={<Setting />} />
        </Route> 

        {/* =========================================
            KHU VỰC KHÁCH/HỘI VIÊN (Không có menu đen)
            ========================================= */}
        <Route path="/member-booking" element={<Booking />} />
        <Route path="/member-packages" element={<Packages />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;