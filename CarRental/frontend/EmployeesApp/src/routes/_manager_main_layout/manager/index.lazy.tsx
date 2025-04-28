import ProtectedRoute from '@components/auth/ProtectedRoute';
import DashboardPage from '@pages/Admin/Dashboard/DashboardPage';
import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute('/_manager_main_layout/manager/')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Manager']} >
      <DashboardPage />
    </ProtectedRoute>
  );
}
