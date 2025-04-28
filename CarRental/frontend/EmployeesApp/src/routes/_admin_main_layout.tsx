import { createFileRoute, Outlet } from '@tanstack/react-router'
import React from 'react';
import AdminMainLayout from '../components/admin/AdminMainLayout';
import { ToastHelper } from '@context/ToastHelper';

export const Route = createFileRoute('/_admin_main_layout')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <React.Fragment>
      <AdminMainLayout>
        <ToastHelper>
          <Outlet />
        </ToastHelper>
      </AdminMainLayout>
    </React.Fragment>
  );
}