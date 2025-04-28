import ProtectedRoute from '@components/auth/ProtectedRoute';
import ClientsPage from '@pages/Operator/Clients/ClientsPage';
import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute(
  '/_operator_main_layout/operator/clients',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Operator']} >
      <ClientsPage />
    </ProtectedRoute>
  );
}
