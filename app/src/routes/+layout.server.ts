import type { userData } from '$lib/userStore.js';

export async function load({ cookies }) {
	const token = cookies.get('jwtToken');
	if (!token) {
		return dataToObject(null, null);
	}

	try {
		const username = JSON.parse(atob(token.split('.')[1])).sub;
		return dataToObject(token, username);
	} catch {
		return dataToObject(token, null);
	}
}

function dataToObject(
	token: string | null,
	username: string | null
): userData {
	return {
		token: token,
		username: username
	};
}
