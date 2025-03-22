#!/usr/bin/env python3
# This is a self-replicating quine program.
def quine():
    s = '''#!/usr/bin/env python3
# This is a self-replicating quine program.
def quine():
    s = {quote}{code}{quote}
    print(s.format(quote=chr(39), code=s))
quine()'''
    print(s.format(quote=chr(39), code=s))
quine()
