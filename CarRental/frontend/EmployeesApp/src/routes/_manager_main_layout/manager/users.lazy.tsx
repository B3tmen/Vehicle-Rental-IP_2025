import { createLazyFileRoute } from '@tanstack/react-router'
import UsersManagementPage from '@pages/Admin/Users/UsersManagementPage'
import ProtectedRoute from '@components/auth/ProtectedRoute'

export const Route = createLazyFileRoute('/_manager_main_layout/manager/users')(
  {
    component: RouteComponent,
  },
)

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Manager']} >
      <UsersManagementPage />
    </ProtectedRoute>
  )
}
