import { faBars, faCarAlt } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import '../styles/Navbar.css'

interface Props{
    toggleSidebar: () => void;
}

const Navbar: React.FC<Props> = ({ toggleSidebar }) =>{
    return(
        <div className="navbar">
            <FontAwesomeIcon 
                icon={faBars}
                onClick={toggleSidebar}
                className='navbar__menu-button'
                tabIndex={0}
                onKeyDown={(e) => {
                    if(e.key === 'Enter'){
                        toggleSidebar();
                    }
                }}
            />

            <div className="navbar__title-container">
                <FontAwesomeIcon icon={faCarAlt} size="3x" className="navbar__title-container__image" />
                <h1 className="navbar__title-container__title">ETFBL_IP</h1>
            </div>
        </div>
    );
}

export default Navbar;