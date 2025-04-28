import { ReactNode, useState } from "react";
import { Button } from "primereact/button";
import { faCar, faCarCrash, faChartPie, faDollar, faHome, faIndustry, faMapMarkedAlt, faTag, faUserPen, faUsers } from "@fortawesome/free-solid-svg-icons";
import CustomSidebar from "@components/CustomSidebar";
import Navbar from "@components/Navbar";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useNavigationHelper } from "@utils/navigationHelper";

type Props = {
    children: ReactNode;
}

const ManagerMainLayout: React.FC<Props> = ({children}) => {
    const navigate = useNavigationHelper();
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    }

    const navigateToPage = (destination: string) => {
        navigate('', destination);
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
                    onClick={() => navigateToPage('/manager')}
                />
                {/* Vehicles */}
                <Button
                    link 
                    label="Vehicles"
                    icon={ <FontAwesomeIcon icon={faCar} /> }
                    className="sidebar__buttons-container--button"
                    onClick={() => navigateToPage('/manager/vehicles')}
                />
                {/* Manufacturers */}
                <Button
                    link 
                    label="Manufacturers"
                    icon={ <FontAwesomeIcon icon={faIndustry} /> }
                    className="sidebar__buttons-container--button"
                    onClick={() => navigateToPage('/manager/manufacturers')}
                />
                {/* Users*/}
                <Button
                    link 
                    label="Users"
                    icon={ <FontAwesomeIcon icon={faUsers} /> }
                    className="sidebar__buttons-container--button"
                    onClick={() => navigateToPage('/manager/users')}
                />

                {/* --------------------------------------------------------------------- */}

                {/* Rentals*/}
                <Button
                    link 
                    label="Rentals"
                    icon={ <FontAwesomeIcon icon={faDollar} /> }
                    className="sidebar__buttons-container--button"
                    onClick={() => navigateToPage('/manager/rentals')}
                />

                {/* Vehicles map */}
                <Button
                    link 
                    label="Vehicle map"
                    icon={ <FontAwesomeIcon icon={faMapMarkedAlt} /> }
                    className="sidebar__buttons-container--button"
                    onClick={() => navigateToPage('/manager/vehicle-map')}
                />

                {/* Clients */}
                <Button
                    link 
                    label="Clients"
                    icon={ <FontAwesomeIcon icon={faUserPen} /> }
                    className="sidebar__buttons-container--button"
                    onClick={() => navigateToPage('/manager/clients')}
                />

                {/* Malfunctions */}
                <Button
                    link 
                    label="Malfunctions"
                    icon={ <FontAwesomeIcon icon={faCarCrash} /> }
                    className="sidebar__buttons-container--button"
                    onClick={() => navigateToPage('/manager/malfunctions')}
                />

                {/* --------------------------------------------------------------------- */}

                {/* Statistics */}
                <Button
                    link 
                    label="Statistics"
                    icon={ <FontAwesomeIcon icon={faChartPie} /> }
                    className="sidebar__buttons-container--button"
                    onClick={() => navigateToPage('/manager/statistics')}
                />

                {/* Rental prices */}
                <Button
                    link 
                    label="Rental prices"
                    icon={ <FontAwesomeIcon icon={faTag} /> }
                    className="sidebar__buttons-container--button"
                    onClick={() => navigateToPage('/manager/rental-prices')}
                />
            </>
        );
    }
    const navigationChildren = renderSidebarChildren();

    return (
        <>
            <Navbar toggleSidebar={toggleSidebar} />
            <CustomSidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} navigationChildren={navigationChildren} />
            <div className={`outlet ${isSidebarOpen ? 'outlet-sidebar-open' : ''}`}>
                {children}
            </div>
        </>
    );
}

export default ManagerMainLayout;