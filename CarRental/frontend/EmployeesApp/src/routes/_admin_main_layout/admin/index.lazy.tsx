import { createLazyFileRoute } from '@tanstack/react-router'
import DashboardPage from '@pages/Admin/Dashboard/DashboardPage';
import ProtectedRoute from '@components/auth/ProtectedRoute';

export const Route = createLazyFileRoute('/_admin_main_layout/admin/')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Administrator']} >
      <DashboardPage />
    </ProtectedRoute>
  );
}
