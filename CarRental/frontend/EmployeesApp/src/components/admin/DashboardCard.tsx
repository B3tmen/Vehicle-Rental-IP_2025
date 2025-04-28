import '@styles/DashboardCard.css'
import { Card } from "primereact/card";

interface Props{
    title: string,
    icon: React.ReactNode,
    quantity: number
}

export const DashboardCard: React.FC<Props> = ({ title, icon, quantity }) => {
    
    return(
        <>
            <Card className='dashboard-card'>
                <div className='dashboard-card__content'>
                    <h2>{title}</h2>
                    {icon}
                    <br />
                    {quantity}
                </div>
            </Card>
        </>
    );
}