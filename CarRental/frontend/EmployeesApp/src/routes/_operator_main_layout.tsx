import { createFileRoute, Outlet } from '@tanstack/react-router'
import React from 'react';
import OperatorMainLayout from '../components/operator/OperatorMainLayout';
import { ToastHelper } from '@context/ToastHelper';

export const Route = createFileRoute('/_operator_main_layout')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <React.Fragment>
      <OperatorMainLayout>
        <ToastHelper>
          <Outlet />
        </ToastHelper>
      </OperatorMainLayout>
    </React.Fragment>
  );
}