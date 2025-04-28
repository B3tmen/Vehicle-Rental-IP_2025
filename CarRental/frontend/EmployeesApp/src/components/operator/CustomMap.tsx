import L from "leaflet";
import { MapContainer, Marker, Popup, TileLayer, useMapEvents } from "react-leaflet";
import carIcon from '@assets/electric-car.png'
import bicycleIcon from '@assets/electric-bicycle.png'
import scooterIcon from '@assets/electric-scooter.png'
import { useState } from "react";
import { MapVehicle } from "types/types";
import { API_OPEN_STREET_MAP_REVERSE_URL, API_OPEN_STREET_MAP_TILE_URL } from "@utils/constants/ApiLinks";

const CarIcon = new L.Icon({
    iconUrl: carIcon,
    iconSize: [40, 40], 
    iconAnchor: [20, 40], 
    popupAnchor: [0, -40], 
});

const BicycleIcon = new L.Icon({
  iconUrl: bicycleIcon,
  iconSize: [40, 40], 
  iconAnchor: [20, 40], 
  popupAnchor: [0, -40], 
});

const ScooterIcon = new L.Icon({
  iconUrl: scooterIcon,
  iconSize: [40, 40], 
  iconAnchor: [20, 40], 
  popupAnchor: [0, -40], 
});

interface Props{
  mapVehicles: MapVehicle[];
}

const CustomMap: React.FC<Props> = ({ mapVehicles }) => {
  // [lat, lng]
  const coordinatesBL = [44.757959, 17.187796];
  const [clickedPosition, setClickedPosition] = useState<[number, number] | null>(null);
  const [address, setAddress] = useState<string | null>(null);
  const [currentPosition, setCurrentPosition] = useState<string | null>(null); 

    
  // Function to fetch address from coordinates
  const fetchAddress = (lat: number, lng: number) => {
    fetch(
      API_OPEN_STREET_MAP_REVERSE_URL + `?lat=${lat}&lon=${lng}&format=json&addressdetails=1`
    )
      .then((response) => response.json())
      .then((data) => {
        if (data && data.address) {
          const city = data.address.city || data.address.town || '';
          const road = data.address.road || 'Unknown road';
          const houseNumber = data.address.house_number ? data.address.house_number : '?';

          setAddress(`${road} ${houseNumber}, ${city}`);
          setCurrentPosition(`${lat}, ${lng}`);
        } else {
          setAddress('Address not found');
        }
      })
      .catch((error) => {
        console.error('Error fetching address:', error);
        setAddress('Error fetching address');
      });
  };

  // Custom component to handle map clicks
  const MapClickHandler = () => {
    useMapEvents({
      click: (event) => {
        const { lat, lng } = event.latlng;
        setClickedPosition([lat, lng]);
        fetchAddress(lat, lng);
      },
    });

    return null;
  };

  // Function to handle vehicle marker clicks
  const handleVehicleClick = (lat: number, lng: number) => {
    setClickedPosition([lat, lng]);
    fetchAddress(lat, lng);
  };

  // Function to determine the correct icon based on vehicle model
  const getVehicleIcon = (type: string) => {
    switch (type.toLowerCase()) {
      case "car":
        return CarIcon;
      case "bicycle":
        return BicycleIcon;
      case "scooter":
        return ScooterIcon;
      default:
        return CarIcon; // Default to car icon if model is unknown
    }
  };


  return (
    <MapContainer
      center={coordinatesBL} // Banja Luka
      zoom={13}
      style={{ height: '100vh', width: '100%' }} // Full screen map
    >
      {/* Add a tile layer */}
      <TileLayer
        url={API_OPEN_STREET_MAP_TILE_URL}
        attribution="&copy; <a href='https://www.openstreetmap.org/copyright'>OpenStreetMap</a> contributors"
      />

      <MapClickHandler />

      {/* //Show marker and popup at clicked position */}
      {/* {clickedPosition && (
          <Marker position={clickedPosition}>
            <Popup>
                <strong>Address:</strong> {address || 'Fetching...'} <br />
                <strong>Position:</strong> {currentPosition || 'Fetching...'}
            </Popup>
          </Marker>
      )} */}

      {/* Render markers for all vehicles */}
      {mapVehicles.map((mapVehicle, index) => (
        <Marker
          key={index}
          position={[mapVehicle.location.latitude, mapVehicle.location.longitude]}
          icon={getVehicleIcon(mapVehicle.vehicle.type_)}
          eventHandlers={{
            click: () => {
              handleVehicleClick(
                mapVehicle.location.latitude,
                mapVehicle.location.longitude
              );
            },
          }}
        >
          <Popup>
            <strong>Model:</strong> {mapVehicle.vehicle.model} <br />
            <strong>Price:</strong> ${mapVehicle.vehicle.price} <br />
            <strong>Address:</strong> {address || 'Fetching...'}
          </Popup>
        </Marker>
      ))}
      
    </MapContainer>
  );
};
  
export default CustomMap;