import ProtectedRoute from '@components/auth/ProtectedRoute';
import RentalPricesPage from '@pages/Manager/RentalPrices/RentalPricesPage';
import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute(
  '/_manager_main_layout/manager/rental-prices',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return(
    <ProtectedRoute allowedRoles={['Manager']} >
      <RentalPricesPage />
    </ProtectedRoute>
  );
}
