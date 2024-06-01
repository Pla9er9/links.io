<script lang="ts">
	import { updateDialog } from '$lib/dialogStore';
	import fetcher from '$lib/fetcher';
	import { Input, InputAddon, ButtonGroup, Button, Helper } from 'flowbite-svelte';
	import { UserCircleSolid, LockSolid, LinkOutline, EnvelopeSolid } from 'flowbite-svelte-icons';
	import { email, maxLength, minLength, required, useForm } from 'svelte-use-form';

	const iconStyles =
		'h-4 w-4 text-gray-500 dark:text-gray-400 h-4 w-4 text-gray-500 dark:text-gray-400';
	const errorMessagesStyles = 'mt-2 font-medium';

	const form = useForm({
		username: { validators: [required, minLength(3), maxLength(22)] },
		email: { validators: [required, email] },
		password: { validators: [required, minLength(6), maxLength(50)] }
	});

	async function register() {
		const res = await fetcher('/api/auth/register', {
			method: 'POST',
			apiUrlPrefix: false,
			body: {
				username: $form.username.value,
				email: $form.email.value,
				password: $form.password.value
			}
		});

		if (!res.ok) {
			const body = await res.json()
			console.error(body)
			alert("Something went wrong")
		}
	}
</script>

<div class="dialog authDialog column">
	<div class="row mt-2">
		<LinkOutline size="xl" color="pink" />
		<i class="ml-2 text-3xl font-semibold text-white">Links.io</i>
	</div>
	<h1 class="text-md mb-8 text-white">Registration</h1>
	<form use:form class="w-full" on:submit={(e) => e.preventDefault()}>
		<div class="w-full">
			<ButtonGroup class="w-full">
				<InputAddon>
					<UserCircleSolid class={iconStyles} />
				</InputAddon>
				<Input placeholder="username" name="username" />
			</ButtonGroup>
		</div>
		{#if !$form.username.valid && $form.username._touched}
			<Helper class={errorMessagesStyles} color="red"
				>Username should be between 3 and 22 characters</Helper
			>
		{/if}
		<div class="w-full">
			<ButtonGroup class="mt-5 w-full">
				<InputAddon>
					<EnvelopeSolid class={iconStyles} />
				</InputAddon>
				<Input placeholder="email" name="email" />
			</ButtonGroup>
		</div>
		{#if !$form.email.valid && $form.email._touched}
			<Helper class={errorMessagesStyles} color="red">Email shoud be valid</Helper>
		{/if}
		<div class="w-full">
			<ButtonGroup class="mt-5 w-full">
				<InputAddon>
					<LockSolid class={iconStyles} />
				</InputAddon>
				<Input placeholder="password" name="password" type="password" />
			</ButtonGroup>
		</div>
		{#if !$form.password.valid && $form.password._touched}
			<Helper class={errorMessagesStyles} color="red"
				>Password should be between 6 and 50 characters</Helper
			>
		{/if}
		<Button on:click={register} disabled={!$form.valid} class="mr-[auto] mt-5 w-full"
			>Sign in</Button
		>
	</form>
	<button on:click={() => updateDialog('login')} class="mt-4 text-sm text-white">
		Already have an account? Sign in!
	</button>
	<p class="my-4 text-xs font-medium text-gray-400">Or Sign up with</p>
	<Button outline color="dark">
		<div class="row w-[120px] justify-center">
			<img src="google_logo.webp" alt="Google logo" />
			Google
		</div>
	</Button>
</div>

<style>
	img {
		width: 18px;
		margin-right: 10px;
	}
</style>
