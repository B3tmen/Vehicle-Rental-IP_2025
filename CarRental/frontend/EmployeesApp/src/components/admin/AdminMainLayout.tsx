import { ReactNode, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCar, faHome, faIndustry, faUsers } from "@fortawesome/free-solid-svg-icons";
import { Button } from "primereact/button";

import Navbar from "../Navbar";
import CustomSidebar from "../CustomSidebar";
import '@styles/AdminMainLayout.css'
import { useNavigationHelper } from "@utils/navigationHelper";

type Props = {
    children: ReactNode;
}

const AdminMainLayout: React.FC<Props> = ({children}) => {
    const navigate = useNavigationHelper();
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    }

    const navigateToDashboard = () => {
        navigate('', '/admin');
    }
    const navigateToVehicles = () => {
        navigate('', '/admin/vehicles');
    }
    const navigateToManufacturers = () => {
        navigate('', '/admin/manufacturers');
    }
    const navigateToUsers = () => {
        navigate('', '/admin/users');
    }

    const renderSidebarChildren = () => {
        return(
            <>
                {/* Dashboard */}
                <Button 
                    link
                    label="Dashboard"
                    icon={ <FontAwesomeIcon icon={faHome} /> }
                    className="sidebar__buttons-container--button"
                    onClick={navigateToDashboard}
                />

                {/* Vehicles */}
                <Button 
                    link
                    label="Vehicles"
                    icon={ <FontAwesomeIcon icon={faCar} /> }
                    className="sidebar__buttons-container--button"
                    onClick={navigateToVehicles}
                />

                {/* Manufacturers */}
                <Button 
                    link
                    label="Manufacturers"
                    icon={ <FontAwesomeIcon icon={faIndustry} /> }
                    className="sidebar__buttons-container--button"
                    onClick={navigateToManufacturers}
                />

                {/* Users*/}
                <Button 
                    link
                    label="Users"
                    icon={ <FontAwesomeIcon icon={faUsers} /> }
                    className="sidebar__buttons-container--button"
                    onClick={navigateToUsers}
                />
            </>
        );
    }
    const navigationChildren = renderSidebarChildren();

    return (
        <>
            <Navbar toggleSidebar={toggleSidebar}/>
            <CustomSidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} navigationChildren={navigationChildren} />
            <div className={`outlet ${isSidebarOpen ? 'outlet-sidebar-open' : ''}`}>
                {children}
            </div>
        </>
    );

}

export default AdminMainLayout;