import '@styles/CustomSidebar.css'
import { useState } from "react";
import { faSignOut } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Button } from "primereact/button";
import { Sidebar } from "primereact/sidebar";

import { useNavigationHelper } from "@utils/navigationHelper";
import ConfirmationDialog from "./dialogs/ConfirmationDialog";
import { logout } from '@api/services/authService';

interface SidebarProps{
    isOpen: boolean;
    toggleSidebar: () => void;
    navigationChildren: React.ReactNode;
}

const CustomSidebar: React.FC<SidebarProps> = ({isOpen, toggleSidebar, navigationChildren }) => {
    const navigate = useNavigationHelper();
    const [logoutVisible, setLogoutVisible] = useState(false);

    const hideLogoutDialog = () => {
        setLogoutVisible(false);
    }
    const showLogoutDialog = () => {
        setLogoutVisible(true);
    }

    
    const navigateToLogin = () => {
        navigate('', '/login', true);
        logout();
    }

    const renderLogoutDialog = () => {
        return(
            <>
                <ConfirmationDialog 
                    header="Logout"
                    visible={logoutVisible}
                    onHide={hideLogoutDialog}
                    onNoAction={hideLogoutDialog}
                    onYesAction={navigateToLogin}
                >
                    <p>Are you sure you want to logout?</p>
                </ConfirmationDialog>
            </>
        );
    }
    const logoutDialog = renderLogoutDialog();

    return (
        <Sidebar 
            visible={isOpen} 
            onHide={toggleSidebar} 
            className="sidebar"
            header="Navigation"
            >
            <div className="sidebar__buttons-container">
                <div className="sidebar__buttons-container__navigation">
                    {navigationChildren}
                </div>

                <div className="sidebar__buttons-container__logout">
                    <Button
                        label="Logout"
                        icon={ <FontAwesomeIcon icon={faSignOut} /> }
                        className="p-button-danger"
                        style={{ width: '100%' }}
                        onClick={showLogoutDialog}
                    />
                </div>
            </div>

            {logoutDialog}
        </Sidebar>
    );
}

export default CustomSidebar;