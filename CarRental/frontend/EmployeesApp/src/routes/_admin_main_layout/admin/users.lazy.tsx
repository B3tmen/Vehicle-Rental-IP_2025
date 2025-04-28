import { createLazyFileRoute } from '@tanstack/react-router'
import UsersManagementPage from '@pages/Admin/Users/UsersManagementPage'
import ProtectedRoute from '@components/auth/ProtectedRoute';

export const Route = createLazyFileRoute('/_admin_main_layout/admin/users')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ProtectedRoute allowedRoles={['Administrator']} >
      <UsersManagementPage />
    </ProtectedRoute>
  ); 
}
