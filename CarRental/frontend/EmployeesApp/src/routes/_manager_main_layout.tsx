import ManagerMainLayout from '@components/manager/ManagerMainLayout';
import { ToastHelper } from '@context/ToastHelper';
import { createFileRoute, Outlet } from '@tanstack/react-router'
import React from 'react';

export const Route = createFileRoute('/_manager_main_layout')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <React.Fragment>
      <ManagerMainLayout>
        <ToastHelper>
          <Outlet />
        </ToastHelper>
      </ManagerMainLayout>
    </React.Fragment>
  );
}
