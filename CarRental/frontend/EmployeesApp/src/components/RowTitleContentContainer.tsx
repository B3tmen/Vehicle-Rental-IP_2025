import '@styles/RowTitleContentContainer.css'

interface Props{
    title: string,
    content: React.ReactNode;
}

export const RowTitleContentContainer: React.FC<Props> = ({title, content}) => {
    return (
        <div className="container">
            <div className="container__side-title">
                <h3>{title}</h3>
            </div>

            <div className="container__content">
                {content}
            </div>
        </div>
    );
}
