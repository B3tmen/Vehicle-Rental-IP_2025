import ProtectedRoute from '@components/auth/ProtectedRoute';
import RentalsPage from '@pages/Operator/Rentals/RentalsPage'
import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute(
  '/_manager_main_layout/manager/rentals',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Manager']} >
      <RentalsPage />
    </ProtectedRoute>
  );
}
