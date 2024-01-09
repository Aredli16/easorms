'use client';

import { useRouter, useSearchParams } from 'next/navigation';
import { signIn, useSession } from 'next-auth/react';
import { useEffect } from 'react';
import Loading from '@/components/Loading';

const Login = () => {
  const searchParams = useSearchParams();
  const { status } = useSession();
  const router = useRouter();

  useEffect(() => {
    if (status === 'unauthenticated')
      signIn('keycloak', { callbackUrl: searchParams.get('callbackUrl') || '/' }).then(console.log);
  }, [searchParams, status]);

  if (status === 'loading')
    return <Loading />;

  if (status === 'authenticated')
    router.push(searchParams.get('callbackUrl') || '/');
};

export default Login;
