import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Homepage from './Homepage';
import Login from './Login'; // Đảm bảo dòng này đã được mở lại

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Đường dẫn trang chủ */}
        <Route path="/" element={<Homepage />} />
        
        {/* Đường dẫn trang Đăng nhập (phải có dòng này thì nút bấm mới hoạt động) */}
        <Route path="/login" element={<Login />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;