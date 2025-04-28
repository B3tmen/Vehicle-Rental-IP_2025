import ProtectedRoute from '@components/auth/ProtectedRoute';
import RentalsPage from '@pages/Operator/Rentals/RentalsPage';
import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute(
  '/_operator_main_layout/operator/rentals',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Operator']} >
      <RentalsPage />
    </ProtectedRoute>
  );
}
