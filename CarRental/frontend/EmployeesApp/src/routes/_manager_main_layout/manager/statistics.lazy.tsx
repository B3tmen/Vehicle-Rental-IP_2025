import ProtectedRoute from '@components/auth/ProtectedRoute';
import StatisticsPage from '@pages/Manager/Statistics/StatisticsPage'
import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute(
  '/_manager_main_layout/manager/statistics',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Manager']} >
      <StatisticsPage />
    </ProtectedRoute>
  );
}
