import { createFileRoute } from '@tanstack/react-router'
import LoginPage from '../../pages/Login/LoginPage'
import { ToastHelper } from '../../context/ToastHelper';

export const Route = createFileRoute('/login/')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ToastHelper >
      <LoginPage />
    </ToastHelper>
  );
}
