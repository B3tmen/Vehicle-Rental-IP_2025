import { createLazyFileRoute } from '@tanstack/react-router'
import ProtectedRoute from '@components/auth/ProtectedRoute';
import DashboardPage from '@pages/Admin/Dashboard/DashboardPage';

export const Route = createLazyFileRoute('/_operator_main_layout/operator/')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Operator']} >
      <DashboardPage />
    </ProtectedRoute>
  );
}
