import fetcher from '$lib/fetcher';
import { error, redirect } from '@sveltejs/kit';

export async function POST(e) {
	const slug = e.params.slug ?? '';

	if (slug !== 'logout') {
		throw error(404)
	}

	e.cookies.delete('jwtToken', { path: '/' });
	return new Response();
}

export async function GET(e) {
	const slug = e.params.slug ?? '';
	
	if (slug !== 'confirm-auth') {
		throw error(404)
	}

	const verificationCode = e.url.searchParams.get('code')
	const userId = e.url.searchParams.get('userId')

	if (verificationCode === null || userId === null) {
		throw redirect(301, "/404")
	}

	const endpoint = `/auth/confirm-auth?verificationCode=${verificationCode}&userId=${userId}`

	const res = await fetcher(endpoint, {
		method: "POST"
	})

	if (!res.ok) {
		throw redirect(301, "/404")
	}

	const data = await res.json()
	
	e.cookies.set('jwtToken', data.token, {
		path: '/',
		httpOnly: true,
		sameSite: 'strict',
		secure: true,
		maxAge: 60 * 60 * 24 * 365 * 10
	});

	throw redirect(301, "/")
}
