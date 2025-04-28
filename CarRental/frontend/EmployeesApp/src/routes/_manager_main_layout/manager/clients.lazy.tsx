import ProtectedRoute from '@components/auth/ProtectedRoute'
import ClientsPage from '@pages/Operator/Clients/ClientsPage'
import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute(
  '/_manager_main_layout/manager/clients',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Manager']} >
      <ClientsPage />
    </ProtectedRoute>
  )
}
