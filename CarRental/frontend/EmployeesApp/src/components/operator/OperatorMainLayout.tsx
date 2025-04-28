import '@styles/AdminMainLayout.css'
import { ReactNode, useState } from 'react';
import { Button } from 'primereact/button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCarCrash, faDollar, faHome, faMapMarkedAlt, faUserPen } from '@fortawesome/free-solid-svg-icons';

import Navbar from '../Navbar';
import CustomSidebar from '../CustomSidebar';
import { useNavigationHelper } from '@utils/navigationHelper';

type Props = {
    children: ReactNode;
}

const OperatorMainLayout: React.FC<Props> = ({children}) => {
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
                        onClick={() => navigateToPage('/operator')}
                    />
                    {/* Rentals*/}
                    <Button
                        link 
                        label="Rentals"
                        icon={ <FontAwesomeIcon icon={faDollar} /> }
                        className="sidebar__buttons-container--button"
                        onClick={() => navigateToPage('/operator/rentals')}
                    />
    
                    {/* Vehicles map */}
                    <Button
                        link 
                        label="Vehicle map"
                        icon={ <FontAwesomeIcon icon={faMapMarkedAlt} /> }
                        className="sidebar__buttons-container--button"
                        onClick={() => navigateToPage('/operator/vehicle-map')}
                    />

                    {/* Clients */}
                    <Button
                        link 
                        label="Clients"
                        icon={ <FontAwesomeIcon icon={faUserPen} /> }
                        className="sidebar__buttons-container--button"
                        onClick={() => navigateToPage('/operator/clients')}
                    />

                    {/* Malfunctions */}
                    <Button
                        link 
                        label="Malfunctions"
                        icon={ <FontAwesomeIcon icon={faCarCrash} /> }
                        className="sidebar__buttons-container--button"
                        onClick={() => navigateToPage('/operator/malfunctions')}
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

export default OperatorMainLayout;