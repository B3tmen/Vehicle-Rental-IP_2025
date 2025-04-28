import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { InputText } from "primereact/inputtext";

import '@styles/Searchbar.css'

interface Props{
    filterValue: string;
    onFilterChange: (e) => void;
}

const Searchbar: React.FC<Props> = ({filterValue, onFilterChange}) => {
    return(
        <div className="search-container">
            <FontAwesomeIcon icon={faSearch} />
            <InputText id="searchbar" placeholder="Search..." value={filterValue} onChange={onFilterChange} />
        </div>
    );
}

export default Searchbar;