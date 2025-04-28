import '@styles/ChartContainer.css'

import { Card } from "primereact/card";

interface Props{
    title: string,
    charts: React.ReactNode
}


export const ChartContainer: React.FC<Props> = ({title, charts}) => {
    return (
        <Card title={title} style={{ justifyContent: 'center', display: 'flex', }} >
            {charts}
        </Card>
    );
}