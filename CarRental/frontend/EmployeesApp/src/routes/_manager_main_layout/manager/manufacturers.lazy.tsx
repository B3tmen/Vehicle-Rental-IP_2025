import { createLazyFileRoute } from '@tanstack/react-router'
import ManufacturersPage from '@pages/Admin/Manufacturers/ManufacturersPage'
import ProtectedRoute from '@components/auth/ProtectedRoute'

export const Route = createLazyFileRoute(
  '/_manager_main_layout/manager/manufacturers',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Manager']} >
      <ManufacturersPage />
    </ProtectedRoute>
  )
}
