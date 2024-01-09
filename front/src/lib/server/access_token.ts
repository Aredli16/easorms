import { authOption } from '@/app/api/auth/[...nextauth]/route';
import { getServerSession } from 'next-auth';

export const getAccessToken = async () => {
  const session = await getServerSession(authOption);

  if (!session) {
    throw 'No session';
  }

  return session.access_token;
};
