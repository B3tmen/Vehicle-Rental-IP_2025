import ProtectedRoute from '@components/auth/ProtectedRoute';
import MalfunctionsPage from '@pages/Operator/Malfunctions/MalfunctionsPage';
import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute(
  '/_operator_main_layout/operator/malfunctions',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Operator']} >
      <MalfunctionsPage />
    </ProtectedRoute>
  );
}
