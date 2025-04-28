import { createLazyFileRoute } from '@tanstack/react-router'
import VehicleManagementPage from '@pages/Admin/Vehicles/VehicleManagement/VehicleManagementPage'
import ProtectedRoute from '@components/auth/ProtectedRoute';

export const Route = createLazyFileRoute('/_admin_main_layout/admin/vehicles/')(
  {
    component: RouteComponent,
  },
)

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Administrator']} >
      <VehicleManagementPage />
    </ProtectedRoute>
  );
}
