import { createLazyFileRoute } from '@tanstack/react-router'
import VehicleManagementPage from '@pages/Admin/Vehicles/VehicleManagement/VehicleManagementPage'
import ProtectedRoute from '@components/auth/ProtectedRoute'

export const Route = createLazyFileRoute(
  '/_manager_main_layout/manager/vehicles/',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Manager']} >
      <VehicleManagementPage />
    </ProtectedRoute>
  )
}
