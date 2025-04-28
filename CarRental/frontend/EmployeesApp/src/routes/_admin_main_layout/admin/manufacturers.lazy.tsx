import { createLazyFileRoute } from '@tanstack/react-router'
import ManufacturersPage from '@pages/Admin/Manufacturers/ManufacturersPage';
import ProtectedRoute from '@components/auth/ProtectedRoute';

export const Route = createLazyFileRoute(
  '/_admin_main_layout/admin/manufacturers',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Administrator']} >
      <ManufacturersPage />
    </ProtectedRoute>
  );
}
