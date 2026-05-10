import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useAuth } from "@/hooks/useAuth";
import { api } from "@/api/api";
import { useRef } from "react";
import { toast } from "sonner";

export interface MembershipInfo {
  id: number;
  packageId: number;
  packageName: string;
  price: number;
  paymentDate: string;
  expiredDate: string;
  status: string;
}

export const useMembership = () => {
  const { user } = useAuth();
  const prevStatus = useRef<string | null>(null); // ← lưu trạng thái trước đó

  return useQuery<MembershipInfo | null>({
    queryKey: ["membership-me", user?.id],
    queryFn: async () => {
      try {
        const data = await api.get<MembershipInfo>("/membership/me");

        // ✅ Nếu trước đó chưa có gói (PENDING/null) mà giờ có PAID → thông báo
        if (prevStatus.current !== "PAID" && data?.status === "PAID") {
          toast.success(`Đăng ký thành công gói ${data.packageName}! 🎉`, {
            duration: 6000,
          });
        }
        prevStatus.current = data?.status ?? null;

        return data;
      } catch {
        prevStatus.current = null;
        return null;
      }
    },
    enabled: !!user,
    staleTime: 1000 * 60 * 5,
    refetchInterval: 1000 * 60 * 5, // polling mỗi 5p
  });
};