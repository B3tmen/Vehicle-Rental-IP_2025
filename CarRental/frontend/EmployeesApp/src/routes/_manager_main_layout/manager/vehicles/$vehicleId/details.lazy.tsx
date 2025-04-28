import { createLazyFileRoute } from '@tanstack/react-router'
import VehicleDetailsPage from '@pages/Admin/Vehicles/VehicleDetails/VehicleDetailsPage'
import ProtectedRoute from '@components/auth/ProtectedRoute';

export const Route = createLazyFileRoute(
  '/_manager_main_layout/manager/vehicles/$vehicleId/details',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <VehicleDetailsPage />
  );
}
