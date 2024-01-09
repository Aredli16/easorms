import { getServerSession } from 'next-auth';
import { authOptions } from '@/app/api/auth/[...nextauth]/auth_options';

export const getAccessToken = async () => {
  const session = await getServerSession(authOptions);

  if (!session) {
    throw 'No session';
  }

  return session.access_token;
};
