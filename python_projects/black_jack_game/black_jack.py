import random as rn

GAME_CARDS = {"User":[],
               "Dealer" : []}

CARDS = ['Ace','2','3','4','5','6','7','8','9','10','JACK','QUEEN','KING']
Cards = CARDS.copy() * 4
CARDS_Value = {'Ace' : 11,'2' : 2,'3' : 3,'4' : 4,'5' : 5,'6' : 6,'7' : 7,
               '8' : 8,'9' : 9,'10' : 10,'JACK' : 10,'QUEEN' : 10,'KING' : 10
                }


States = {
    "WIN" : 0,
    "PLAYED" : 0,
    "LOOSE" : 0,
    "TOTAL_BALANCE" : 200,
    "Current_bet_user" : 0,
    "Current_bet_user1" : 0,
    "TIE" : 0
    }


def menu(options):

    """ it takes the list of options available at a specific time. it displays those options
    on the player screen and let him choose one of them. asks again and again unless they choose one of them.
    Then returns the choice."""

    print("\n\n____________________________________________________\n\n")
    for i in range(len(options)):
        print(f"{i+1}.{options[i]}.")
    choice = int (input("Choose one of the options Above: "))

    while not (0 < choice <= len(options)):
        print("\n       Wrong Choice. Try Again           \n")
        for i in range(len(options)):
            print(f"{i + 1}.{options[i]}.")
        choice = int(input("Choose one of the options Above: "))

    print("\n\n____________________________________________________\n\n")
    return choice

def get_next_options(hand,split_check,user):

    """ it returns the list of moves a player has at the time he has to make a move.
    if score is 21 then no more moves are allowed so Empty List. if First two are same then split is also available.
    Also, if the sum of players card is in between 9 and 11 then double down Move is also allowed"""

    if (hand[0] == hand[1] and len(hand) == 2 and
            not split_check and
            States["Current_bet_" + (str(user)).lower()]  <= States["TOTAL_BALANCE"]):

        return ["Hit","Stand","Split"]
    total = get_total(hand)
    if total == 21:
        return []
    if 9 <= total <= 11 and States["Current_bet_" + (str(user)).lower()] * 2 <= States["TOTAL_BALANCE"]:
        return ["Hit","Stand","Double Down"]
    else:
        return ["Hit","Stand"]

def initial_cards():

    """ it distributes the initial two cards to each the player and to the computer at the Start of GamePlay"""
    rn.shuffle(Cards)
    index1 = rn.randint(0,len(Cards) - 1)
    index2 = rn.randint(0,len(Cards) - 2)
    users_cards = [Cards[index1],Cards[index2]]
    Cards.remove(Cards[index1])
    Cards.remove(Cards[index2])




    index1 = rn.randint(0, len(Cards) - 3)
    index2 = rn.randint(0, len(Cards) - 4)
    dealers_cards = [Cards[index1],Cards[index2]]
    Cards.remove(Cards[index1])
    Cards.remove(Cards[index2])

    GAME_CARDS['User'] = users_cards
    GAME_CARDS['Dealer'] = dealers_cards

def show_initial_cards(user):
    f""" user is the current hand playing the game. 
        It prints the cards On the current hand on Players screen when player is playing 
        with one card of computer faced down.If Computer has an Ace then that card is faced Up."""

    print(f"\n\nCurrent Bet: {States["Current_bet_" + (str(user)).lower()]}")
    print("-------------------------------------------------------------------")

    user_result = "Your Cards are: "

    for card in GAME_CARDS[user]:
        user_result += f"{card} "

    print(user_result)

    if CARDS_Value[GAME_CARDS['Dealer'][0]] == 11 or CARDS_Value[GAME_CARDS['Dealer'][1]] == 11:
        if CARDS_Value[GAME_CARDS['Dealer'][0]] == 11:
            print(f"Dealers Cards: {GAME_CARDS['Dealer'][0]} , 'Hidden Card'")
        else:
            print(f"Dealers Cards: {GAME_CARDS['Dealer'][1]} , 'Hidden Card'")
    else:
        print(f"Dealers Cards: {GAME_CARDS['Dealer'][0]} , 'Hidden Card'")

    print("-------------------------------------------------------------------")


def show_cards(split_check):

    f""" split_check is a boolean value either the game play is Split game pLay or simple. 
    It prints the cards On the Players screen when dealer is playing.
    if There are two hands after split game play then both hands along with all the cards of Computer"""

    if split_check:
        print(f"\n\nCurrent Bet: {States["Current_bet_user"] + States["Current_bet_user1"]}")
        print("-------------------------------------------------------------------")
        user_result1 = "Your First Hand Cards are: "
        for card in GAME_CARDS['User']:
            user_result1 += f"{card} "
        print(user_result1)

        user_result1 = "Your Second Hand Cards are: "
        for card in GAME_CARDS['User1']:
            user_result1 += f"{card} "
        print(user_result1)

        dealer_result = "Dealer's Cards are: "
        for card in GAME_CARDS['Dealer']:
            dealer_result += f"{card} "
        print(dealer_result)
    else:
        print(f"\n\nCurrent Bet: {States["Current_bet_user"]}")
        print("-------------------------------------------------------------------")
        user_result = "Your Cards are: "
        for card in GAME_CARDS['User']:
            user_result += f"{card} "
        print(user_result)

        dealer_result = "Dealer's Cards are: "
        for card in GAME_CARDS['Dealer']:
            dealer_result += f"{card} "
        print(dealer_result)

    print("-------------------------------------------------------------------")



def decide_winner(user):

    """It basically decides the winner and prints the result on the Players screen"""

    bet = States["Current_bet_" + (str(user)).lower()]
    if get_total(GAME_CARDS[user]) > get_total(GAME_CARDS['Dealer']):
        States['WIN'] += 1
        States["TOTAL_BALANCE"] += (bet * 2)
        print("You win.")
    elif get_total(GAME_CARDS[user]) == get_total(GAME_CARDS['Dealer']):
        States["TIE"] += 1
        States["TOTAL_BALANCE"] += bet
        print("Tie")
    else:
        States["LOOSE"] += 1
        print("You Loose.")





def play_game():

    """This manages whole game play first let the player play and then allow computer to play if needed.
    prints the result and done"""


    States["PLAYED"] += 1
    bet = get_bet()
    States["Current_bet_user"] = bet
    States["TOTAL_BALANCE"] -= bet
    val = play_user('User',False)

    if val:
        if get_total(GAME_CARDS['User']) > 21:
            print("You Loose.")
            States["LOOSE"] += 1
            return

        play_dealer(False)

        if get_total(GAME_CARDS['Dealer']) > 21:
            print("You win")
            States['WIN'] += 1
            States["TOTAL_BALANCE"] += (States["Current_bet_user"] * 2)

            return
        decide_winner('User')

    else:

        States["PLAYED"] += 1
        check1 = False
        check2 = False
        if get_total(GAME_CARDS['User']) > 21:
            print("You Loose in First Hand.")
            States["LOOSE"] += 1
            check1 = True

        if get_total(GAME_CARDS['User1']) > 21:
            States["LOOSE"] += 1
            print("You Loose Second Hand.")
            check2 = True


        if check2 & check1:
            return

        play_dealer(True)

        if get_total(GAME_CARDS['Dealer']) > 21:
            print("----  dealer Busts. ----")
            if check1:
                print("You Loose first Hand.")
            else:
                print("You win First hand")
                States['WIN'] += 1
                States["TOTAL_BALANCE"] += (States["Current_bet_user"] * 2)

            if check2:
                print("You Loose second Hand.")
            else:
                print("YOu Win Second Hand.")
                States['WIN'] += 1
                States["TOTAL_BALANCE"] += (States["Current_bet_user1"] * 2)
            return

        if check1 :
            decide_winner('User1')
        elif check2:
            decide_winner('User')
        else:
            print("Hand1 Result: ")
            decide_winner('User')
            print("Hand2 Result: ")
            decide_winner('User1')



def play_user(user,split_check):

    """It manages Players Game play distributing cards asking for the next move unless busts or stand.
    Manages split or double down game play if available.Split_check controls the split game play."""

    if not split_check:
        initial_cards()

    show_initial_cards(user)
    stand = False
    while get_total(GAME_CARDS[user]) <= 21 and not stand:
        options = get_next_options(GAME_CARDS[user],split_check,user)

        if len(options) == 0:
            print("You cards sum upto 21 so you Must STAND Here.")
            break
        else:
            choice = menu(options)
            match choice:
                case 1:
                    card = hit()
                    GAME_CARDS[user].append(card)
                    show_initial_cards(user)
                case 2:
                    stand = True
                case 3:
                    print("Bet is Doubled.")
                    States["TOTAL_BALANCE"] -= States["Current_bet_" + (str(user)).lower()]
                    if options[2] == "Split" and not split_check:
                        States["Current_bet_user1"] = States["Current_bet_user"]
                        split_game_play()
                        return False
                    else:
                        States["TOTAL_BALANCE"] -= States["Current_bet_" + (str(user)).lower()]
                        States["Current_bet_" + (str(user)).lower()] *= 2
                        card = hit()
                        GAME_CARDS[user].append(card)
                        show_initial_cards(user)
                        break

    return True

def play_dealer(split_check):

    """Computer keeps playing unless busts os score is in between 17 and 21"""
    show_cards(split_check)
    while get_total(GAME_CARDS['Dealer']) < 17:
        card = hit()
        GAME_CARDS['Dealer'].append(card)
        show_cards(split_check)


def split_game_play():
    """it Handles the split gamePlay let the player first play for first hand and then fo the other hand"""
    hand1 = [GAME_CARDS['User'][0],hit()]
    hand2 = [GAME_CARDS['User'][0],hit()]

    GAME_CARDS['User'] = hand1
    GAME_CARDS['User1'] = hand2

    print("----------------Lets Play for the First Hand First. ----------------------")
    play_user('User',True)
    print("\nDone With First Hand.\n")
    print("----------------Lets Play for the Second Hand Now. ----------------------")
    play_user('User1',True)
    print("\nDone with second Hand.\n")


def get_total(cards):

    """Returns the Sum of cards depending on the situation. If adding ace to sum takes the result over 21 then
    it is counted as 1"""
    num = 0
    count = 0
    for card in cards:
        if card == "Ace":
            count += 1
            continue
        num += CARDS_Value[card]

    if count == 1:
        if num + 11 <= 21:
            num += 11
        else:
            num += 1

    elif count > 1:
        if num + 11 < 21:
            num += 11
        else:
            num += 1
        count -= 1
        num += count


    return num


def hit():
    """It Randomly picks a card and adds it to the player or computers data on making a hit move"""
    index = rn.randint(0,len(Cards) - 1 )
    card = Cards[index]
    Cards.remove(card)
    return card

def play_again():
    """Asks the Player if he want to play again or not."""
    choice = input("---------You Want to Play Again y or n: ")
    while len(choice) != 1 or (choice != 'y' and choice != 'n'):
        print("Invalid Choice. Try Again")
        choice = input("---------You Want to Play Again y or n: ")


    return choice == 'y'

def show_states():
    """Prints the Current States on the Player Screen"""
    print("Current Stats: ")
    print(f"Played : {States["PLAYED"]} , WIN : {States["WIN"]} , Loose : {States["LOOSE"]}")
    print(f"Current Balance: {States["TOTAL_BALANCE"]}")

def program():
    """Main game play"""
    options = ["Play the Game","Exit"]

    while True:
        show_states()
        choice = menu(options)
        match choice:
            case 1:
                check = True
                while check:
                    play_game()
                    show_states()
                    check = play_again()
                    Cards = CARDS.copy() * 4
                    GAME_CARDS.clear()
            case 2:
                return


def get_bet():
    """Asks the player the amount they want to play bet for."""
    response = input("Enter your Bet: ")
    while (not response.isnumeric()) or (not (0 < int(response) <= States["TOTAL_BALANCE"])):
        if (not response.isnumeric()) | 0 < int(response):
            print("Invalid Bet.")
        else:
            print("Insufficient Balance. ")
        response = input("Enter your Bet: ")

    return int(response)

program()