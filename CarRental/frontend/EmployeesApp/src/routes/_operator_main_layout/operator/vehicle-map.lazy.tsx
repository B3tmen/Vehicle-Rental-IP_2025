import { createLazyFileRoute } from '@tanstack/react-router'
import VehicleMapPage from '../../../pages/Operator/VehicleMap/VehicleMapPage';
import ProtectedRoute from '@components/auth/ProtectedRoute';

export const Route = createLazyFileRoute(
  '/_operator_main_layout/operator/vehicle-map',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Operator']} >
      <VehicleMapPage />
    </ProtectedRoute>
  );
}
