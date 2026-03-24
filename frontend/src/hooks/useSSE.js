import { useEffect, useRef, useState } from 'react';

export default function useSSE(url, eventTypes) {
	const [events, setEvents] = useState([]);
	const [isConnected, setIsConnected] = useState(false);
	const sourceRef = useRef(null);

	useEffect(() => {
		const source = new EventSource(url);
		sourceRef.current = source;

		source.onopen = () => {
			setIsConnected(true);
		};

		source.onerror = (error) => {
			console.error('SSE connection error', error);
			setIsConnected(false);
		};

		eventTypes.forEach((typeEvent) => {
			source.addEventListener(typeEvent, (event) => {
				const parsed = JSON.parse(event.data);

				setEvents((prevEvents) => [
					...prevEvents,
					{
						id: event.lastId,
						type: typeEvent,
						topic: parsed.topic,
						data: parsed.data,
						timestamp: new Date().toLocaleTimeString(),
					},
				]);
			});
		});

		return () => {
			source.close();
		};
	}, [url, eventTypes.join(',')]);

	return { events, isConnected };
}
