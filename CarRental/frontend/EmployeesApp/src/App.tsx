import './App.css'
import { createRouter, RouterProvider } from '@tanstack/react-router'
import { routeTree } from './routeTree.gen';
import { PrimeReactProvider } from 'primereact/api';
import 'primeicons/primeicons.css';
import { AuthProvider } from './context/AuthProvider';


const router = createRouter({ routeTree });

function App() {

  return (
    <PrimeReactProvider>
      <AuthProvider>
        <RouterProvider router={router} />
      </AuthProvider>
    </PrimeReactProvider>
  )
}

export default App
