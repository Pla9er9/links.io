<script lang="ts">
	import fetcher from "$lib/fetcher";
	import { maxLength, minLength, required, useForm } from "svelte-use-form";
	import ChangeAccountPropertyDialog from "./ChangeAccountPropertyDialog.svelte";
	import { toastStore, type toast } from "$lib/toastStore";
	import { ButtonGroup, Helper, Input, InputAddon } from "flowbite-svelte";
	import { LockSolid } from "flowbite-svelte-icons";
	import { get } from "svelte/store";
	import { userStore } from "$lib/userStore";
	import { dialogStroe } from "$lib/dialogStore";

    
	const iconStyles =
		'h-4 w-4 text-gray-500 dark:text-gray-400 h-4 w-4 text-gray-500 dark:text-gray-400';
	const errorMessagesStyles = 'mt-2 font-medium';
    const passwordValidators = [required, minLength(6), maxLength(50)]

    const form = useForm({
		currentPassword: { validators: passwordValidators },
		newPassword: { validators: passwordValidators },
	});

    async function submit() {
		const res = await fetcher('/account/password', {
			method: 'PUT',
			body: {
                password: $form.newPassword.value,
				currentPassword: $form.currentPassword.value,
			},
            token: get(userStore).token ?? ''
		});

		let t: toast;

		if (res.ok) {
			t = {
				title: 'Succes',
				text: 'Password changed',
				variant: 'succes'
			};

            dialogStroe.update(d => null)
		}
        else if (res.status == 403) {
            t = {
                title: 'Bad credentials',
                text: 'Wrong current password',
                variant: 'danger'
            };
        }
        else {
            t = {
                title: 'Unknown Error',
                text: 'Unknown Error occurept, try later',
                variant: 'danger'
            };
		}

		toastStore.update(toasts => {
			toasts.push(t)
			return toasts
		})
	}
    
</script>
<ChangeAccountPropertyDialog
    property="Password"
    form={form}
    onSubmit={submit}
>
    <div class="w-full">
        <ButtonGroup class="mt-5 w-full">
            <InputAddon>
                <LockSolid class={iconStyles} />
            </InputAddon>
            <Input placeholder="Current password" name="currentPassword" autocomplete="password" type="password" />
        </ButtonGroup>
    </div>
    {#if !$form.currentPassword.valid && $form.currentPassword._touched}
        <Helper class={errorMessagesStyles} color="red"
            >Password should be between 6 and 50 characters</Helper
        >
    {/if}
    <div class="w-full">
        <ButtonGroup class="mt-5 w-full">
            <InputAddon>
                <LockSolid class={iconStyles} />
            </InputAddon>
            <Input placeholder="New password" name="newPassword" autocomplete="password" type="password" />
        </ButtonGroup>
    </div>
    {#if !$form.newPassword.valid && $form.newPassword._touched}
        <Helper class={errorMessagesStyles} color="red"
            >Password should be between 6 and 50 characters</Helper
        >
    {/if}
</ChangeAccountPropertyDialog>