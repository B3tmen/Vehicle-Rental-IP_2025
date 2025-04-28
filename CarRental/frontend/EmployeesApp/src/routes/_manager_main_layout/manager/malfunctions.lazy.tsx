import ProtectedRoute from '@components/auth/ProtectedRoute'
import MalfunctionsPage from '@pages/Operator/Malfunctions/MalfunctionsPage'
import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute(
  '/_manager_main_layout/manager/malfunctions',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Manager']} >
      <MalfunctionsPage />
    </ProtectedRoute>
  )
}
