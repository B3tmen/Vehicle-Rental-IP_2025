import '@styles/ChartTitleContainer.css'

interface Props{
    title: string;
    chart: React.ReactNode;
}

export const ChartTitleContainer: React.FC<Props> = ({ title, chart}) =>{
    return (
        <div className="chart-container">
            <div className="chart-container__title">
                <h3>{title}</h3>
            </div>

            <div className="chart-container__chart">
                {chart}
            </div>
        </div>
    );
}