import { createFileRoute } from '@tanstack/react-router'
import { useNavigationHelper } from '../utils/navigationHelper';

export const Route = createFileRoute('/')({
  component: RouteComponent,
})

function RouteComponent() {
  const navigate = useNavigationHelper();

  const navigateToLogin = () => {
    navigate('', '/login');
  }
  
  return (
    <>
      {navigateToLogin()}
    </>
  );

}
