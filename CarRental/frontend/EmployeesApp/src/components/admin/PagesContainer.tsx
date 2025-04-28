import '@styles/PagesContainer.css'
import { Divider } from "primereact/divider";


interface Props{
    headerTitleChildren: React.ReactNode;
    headerButtonsChildren?: React.ReactNode;
    mainContentChildren: React.ReactNode;
}

const PagesContainer: React.FC<Props> = ({headerTitleChildren, headerButtonsChildren, mainContentChildren}) => {
    return (
        <>
            <div className="page-container">
                <div className="page-container__header">
                    <div className="page-container__header__title-buttons">
                        <div className="page-container__header__title-buttons__title">
                            {headerTitleChildren}
                        </div>

                        <div className="page-container__header__title-buttons__buttons">
                            {headerButtonsChildren}
                        </div>
                    </div>
                </div>

                <Divider style={{ marginBottom: '5px' }} />

                <div className="page-container__main-content">
                    {mainContentChildren}
                </div>
            </div>
        </>
    );
}
export default PagesContainer;