import { createLazyFileRoute } from '@tanstack/react-router'
import VehicleMapPage from '../../../pages/Operator/VehicleMap/VehicleMapPage'
import ProtectedRoute from '@components/auth/ProtectedRoute'

export const Route = createLazyFileRoute(
  '/_manager_main_layout/manager/vehicle-map',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Manager']}>
      <VehicleMapPage />
    </ProtectedRoute>
  )
}
