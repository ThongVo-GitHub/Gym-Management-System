import React, { useState, useEffect } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogFooter, DialogTitle, DialogDescription } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Edit3, Trash2, Plus, Zap, Clock } from 'lucide-react';
import packageApi from '@/api/packageAPI';

const Packages = () => {
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editOpen, setEditOpen] = useState(false);
  const [selectedPackage, setSelectedPackage] = useState(null);
  const [editForm, setEditForm] = useState({
    packageName: '',
    price: '',
    durationMonths: '',
    description: '',
  });
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState('');

  const openEditPackage = (pkg) => {
    setSelectedPackage(pkg);
    setEditForm({
      packageName: pkg.name || '',
      price: pkg.price?.toString() ?? '',
      durationMonths: pkg.durationMonths?.toString() ?? '',
      description: pkg.description || '',
    });
    setFormError('');
    setEditOpen(true);
  };

  const closeEditModal = () => {
    setEditOpen(false);
    setSelectedPackage(null);
    setFormError('');
  };

  const handleEditChange = (event) => {
    const { name, value } = event.target;
    setEditForm((prev) => ({ ...prev, [name]: value }));
  };

  const openCreatePackage = () => {
    setSelectedPackage(null);
    setEditForm({
      packageName: '',
      price: '',
      durationMonths: '',
      description: '',
    });
    setFormError('');
    setEditOpen(true);
  };

  const handleCreatePackage = async (event) => {
    event.preventDefault();
    if (!editForm.packageName.trim()) {
      setFormError('Tên gói không được để trống.');
      return;
    }
    if (!editForm.price || Number(editForm.price) <= 0) {
      setFormError('Giá phải lớn hơn 0.');
      return;
    }
    if (!editForm.durationMonths || Number(editForm.durationMonths) < 1) {
      setFormError('Thời hạn phải từ 1 tháng trở lên.');
      return;
    }

    setSaving(true);
    setFormError('');
    try {
      const payload = {
        packageName: editForm.packageName,
        price: Number(editForm.price),
        durationMonths: Number(editForm.durationMonths),
        description: editForm.description,
      };
      const response = await packageApi.create(payload);
      const created = response.data;

      setPackages((prev) => [
        {
          id: created.id,
          name: created.packageName,
          price: created.price,
          durationMonths: created.durationMonths,
          description: created.description || '',
          status: 'ACTIVE',
          features: (created.description || 'Gói tập chất lượng cao').split('.').filter(Boolean).map((item) => item.trim()),
          color: 'from-red-500 to-orange-500',
          icon: <Zap size={18} />,
        },
        ...prev,
      ]);
      closeEditModal();
    } catch (error) {
      console.error('Create package failed:', error);
      setFormError(error.response?.data?.message || 'Tạo gói tập thất bại.');
    } finally {
      setSaving(false);
    }
  };

  const handleUpdatePackage = async (event) => {
    event.preventDefault();
    if (!selectedPackage) return;

    setSaving(true);
    setFormError('');
    try {
      const payload = {
        packageName: editForm.packageName,
        price: Number(editForm.price),
        durationMonths: Number(editForm.durationMonths),
        description: editForm.description,
      };
      const response = await packageApi.update(selectedPackage.id, payload);
      const updated = response.data;

      setPackages((prev) =>
        prev.map((pkg) =>
          pkg.id === selectedPackage.id
            ? {
                ...pkg,
                name: updated.packageName,
                price: updated.price,
                durationMonths: updated.durationMonths,
                description: updated.description || '',
                features: (updated.description || '')
                  .split('.')
                  .filter(Boolean)
                  .map((item) => item.trim()),
              }
            : pkg,
        ),
      );
      closeEditModal();
    } catch (error) {
      console.error('Update package failed:', error);
      setFormError(error.response?.data?.message || 'Cập nhật thất bại.');
    } finally {
      setSaving(false);
    }
  };

  const handleDeletePackage = async (pkg) => {
    if (!window.confirm(`Bạn có chắc muốn xóa gói "${pkg.name}" không?`)) return;
    try {
      await packageApi.delete(pkg.id);
      setPackages((prev) => prev.filter((item) => item.id !== pkg.id));
    } catch (error) {
      console.error('Delete package failed:', error);
    }
  };

  useEffect(() => {
    const fetchPackages = async () => {
      try {
        const response = await packageApi.getAll();
        setPackages(response.data.map((pkg) => ({
          id: pkg.id,
          name: pkg.packageName,
          price: pkg.price,
          durationMonths: pkg.durationMonths,
          description: pkg.description || '',
          status: pkg.status || 'ACTIVE',
          features: (pkg.description || 'Gói tập chất lượng cao').split('.').filter(Boolean).map((item) => item.trim()),
          color: pkg.durationMonths >= 12 ? 'from-yellow-500 to-amber-600' : pkg.durationMonths >= 6 ? 'from-red-500 to-orange-500' : 'from-blue-500 to-cyan-500',
          icon: <Zap size={18} />,
        })));
      } catch (error) {
        setError('Không thể tải dữ liệu gói tập.');
      } finally {
        setLoading(false);
      }
    };
    fetchPackages();
  }, []);

  return (
    <main className="flex-1 flex flex-col h-screen overflow-hidden font-sans relative text-white">
      <div className="absolute inset-0 z-0" style={{ backgroundImage: "url('/bgmemdash.png')", backgroundSize: 'cover', backgroundPosition: 'center' }}>
        <div className="absolute inset-0 bg-black/85 backdrop-blur-[4px]"></div>
      </div>

      <div className="relative z-10 flex-1 overflow-auto p-8 lg:p-12">
        <div className="flex flex-col md:flex-row justify-between items-end mb-12 gap-6">
          <div>
            <h2 className="text-3xl font-black uppercase italic tracking-wider">
              Thiết lập <span className="text-red-600">Gói Dịch Vụ</span>
            </h2>
            <div className="h-1 w-20 bg-red-600 mt-2 rounded-full mb-4"></div>
            <p className="text-gray-400 text-xs font-bold uppercase tracking-widest">Quản lý giá và quyền lợi các gói hội viên</p>
          </div>

          <button
            type="button"
            onClick={openCreatePackage}
            className="flex items-center gap-2 bg-white text-black hover:bg-red-600 hover:text-white px-6 py-3 rounded-2xl font-black text-xs uppercase tracking-widest transition-all shadow-xl active:scale-95"
          >
            <Plus size={18} /> Tạo gói mới
          </button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 max-w-7xl mx-auto">
          {loading ? (
            <div className="col-span-full text-center text-gray-400 py-8 text-xs uppercase font-black tracking-widest">Đang tải...</div>
          ) : (
            packages.map((pkg) => (
              <div key={pkg.id} className="group bg-white/5 backdrop-blur-2xl border border-white/10 rounded-[2.5rem] p-8 transition-all hover:border-red-600/50 shadow-2xl flex flex-col">
                <div className="flex justify-between items-start mb-6">
                  <span className="text-[9px] font-black px-3 py-1 rounded-full uppercase tracking-widest border bg-white/5 border-white/10 text-gray-400">
                    {pkg.status}
                  </span>
                  <div className="flex gap-2">
                    <button onClick={() => openEditPackage(pkg)} className="p-2 bg-white/5 rounded-lg hover:bg-blue-600 transition-colors"><Edit3 size={14} /></button>
                    <button onClick={() => handleDeletePackage(pkg)} className="p-2 bg-white/5 rounded-lg hover:bg-red-600 transition-colors"><Trash2 size={14} /></button>
                  </div>
                </div>

                <div className="flex flex-col mb-6">
                  <div className="flex items-center gap-1.5 mb-2 text-blue-400">
                    <Clock size={12} />
                    <span className="text-[10px] font-black uppercase tracking-wider">{pkg.durationMonths} Tháng</span>
                  </div>
                  <div className="flex items-center gap-4">
                    <div className={`w-12 h-12 rounded-xl bg-gradient-to-br ${pkg.color} flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform`}>
                      {pkg.icon}
                    </div>
                    <h3 className="text-xl font-black uppercase tracking-tight">{pkg.name}</h3>
                  </div>
                </div>

                <div className="bg-black/20 rounded-2xl p-4 mb-6 border border-white/5">
                  <p className="text-[9px] text-gray-500 font-black uppercase mb-1">Giá hiển thị:</p>
                  <div className="flex items-baseline gap-1">
                    <span className="text-3xl font-black text-white">{Number(pkg.price || 0).toLocaleString('vi-VN')}</span>
                    <span className="text-red-500 font-black text-lg italic">₫</span>
                    <span className="text-gray-500 text-[10px] font-bold uppercase ml-1">/ {pkg.durationMonths} tháng</span>
                  </div>
                </div>

                <div className="space-y-2 mb-4 flex-1">
                  <p className="text-[9px] text-gray-500 font-black uppercase mb-3">Quyền lợi gói:</p>
                  <div className="flex flex-wrap gap-2">
                    {pkg.features.map((feat, i) => (
                      <span key={i} className="text-[10px] font-bold bg-white/5 border border-white/10 px-3 py-1 rounded-lg text-gray-300">
                        {feat}
                      </span>
                    ))}
                  </div>
                </div>
              </div>
            ))
          )}
        </div>

        <Dialog open={editOpen} onOpenChange={setEditOpen}>
          <DialogContent className="bg-zinc-900 border-white/10 text-white">
            <DialogHeader>
              <DialogTitle className="text-white">{selectedPackage ? 'Chỉnh sửa gói tập' : 'Tạo gói tập mới'}</DialogTitle>
              <DialogDescription className="text-gray-400">Nhập thông tin chi tiết cho gói dịch vụ TwelveFit.</DialogDescription>
            </DialogHeader>
            <form onSubmit={selectedPackage ? handleUpdatePackage : handleCreatePackage} className="space-y-4">
              <div>
                <label className="block text-xs font-black uppercase mb-2 text-gray-400">Tên gói</label>
                <Input className="bg-white/5 border-white/10" name="packageName" value={editForm.packageName} onChange={handleEditChange} />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-black uppercase mb-2 text-gray-400">Giá (₫)</label>
                  <Input className="bg-white/5 border-white/10" name="price" type="number" value={editForm.price} onChange={handleEditChange} />
                </div>
                <div>
                  <label className="block text-xs font-black uppercase mb-2 text-gray-400">Thời hạn (tháng)</label>
                  <Input className="bg-white/5 border-white/10" name="durationMonths" type="number" value={editForm.durationMonths} onChange={handleEditChange} />
                </div>
              </div>
              <div>
                <label className="block text-xs font-black uppercase mb-2 text-gray-400">Mô tả (Dùng dấu chấm để tách các tính năng)</label>
                <Textarea className="bg-white/5 border-white/10" name="description" value={editForm.description} onChange={handleEditChange} />
              </div>
              {formError && <p className="text-xs text-red-400 font-bold uppercase">{formError}</p>}
              <DialogFooter className="gap-2">
                <button type="button" onClick={closeEditModal} className="rounded-xl border border-white/10 bg-white/5 px-6 py-2 text-xs font-black uppercase text-gray-400 hover:bg-white/10">Hủy</button>
                <button type="submit" disabled={saving} className="rounded-xl bg-red-600 px-6 py-2 text-xs font-black uppercase text-white hover:bg-red-500 disabled:opacity-50">
                  {saving ? 'Đang lưu...' : (selectedPackage ? 'Cập nhật' : 'Tạo mới')}
                </button>
              </DialogFooter>
            </form>
          </DialogContent>
        </Dialog>
      </div>
    </main>
  );
};

export default Packages;